package com.raenjamio.valtech.testdrive.api.controller.v1;

import java.util.concurrent.ExecutionException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.raenjamio.valtech.testdrive.api.v1.criteria.ReservationCriteria;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.PageReservation;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.service.ReservationService;
import com.raenjamio.valtech.testdrive.api.v1.service.query.ReservationQueryService;
import com.raenjamio.valtech.testdrive.exceptions.BadRequestAlertException;

import lombok.extern.slf4j.Slf4j;
/**
 * Punto de entrada de las reservas de la aplicacion
 * @author raenjamio
 *
 */
@Slf4j
@RestController
public class ReservationController {
	
	public static final String BASE_URL = "/api/v1/reservations";
	
	private final ReservationQueryService reservationQueryService;
	
	private final ReservationService reservationService;
	
	
	/**
	 * Instantiates a new reservation controller.
	 *
	 * @param reservationQueryService the reservation query service
	 * @param reservationService the reservation service
	 */
	public ReservationController(ReservationQueryService reservationQueryService,
			ReservationService reservationService) {
		super();
		this.reservationQueryService = reservationQueryService;
		this.reservationService = reservationService;
	}
	
	/**
	 * Gets the all.
	 *
	 * @param criteria the criteria
	 * @param pageable the pageable
	 * @return the all
	 */
	@GetMapping(ReservationController.BASE_URL)
	@ResponseStatus(HttpStatus.OK)
	public PageReservation getAll(ReservationCriteria criteria, Pageable pageable) {
		log.debug("REST request to get reservations by criteria: {}", criteria);
		Page<ReservationDTO> page = reservationQueryService.findByCriteria(criteria, pageable);
		return new PageReservation(page);
	}
	
	/**
	 * Gets the reservation.
	 *
	 * @param id the id
	 * @return the reservation
	 */
	@GetMapping(ReservationController.BASE_URL + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationDTO getReservation(@PathVariable Long id){
		log.debug("@getProfile id: " + id);
        return reservationService.findById(id);
    }
	
	/**
	 * Delete.
	 *
	 * @param id the id
	 */
	@DeleteMapping( ReservationController.BASE_URL + "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		log.debug("REST request to delete reservation : {}", id);
		reservationService.deleteById(id);
	}

	/**
	 * Update.
	 *
	 * @param id the id
	 * @param reservationDTO the reservation DTO
	 * @return the reservation DTO
	 */
	@PutMapping(ReservationController.BASE_URL + "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ReservationDTO update(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
		log.debug("REST request to update reservation id: {} reservation {}", id, reservationDTO);
		if (id == null) {
			throw new BadRequestAlertException("Falta el id del recurso", "reservation", "id.null");
		}
		return reservationService.saveByDTO(id, reservationDTO);
	}

	/**
	 * Patch.
	 *
	 * @param id the id
	 * @param reservationDTO the reservation DTO
	 * @return the reservation DTO
	 */
	@PatchMapping(ReservationController.BASE_URL + "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ReservationDTO patch(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
		log.debug("REST request to patch reservation id: {} reservation {}", id, reservationDTO);
		return reservationService.patch(id, reservationDTO);
	}
	
	/**
	 * Reservation car.
	 *
	 * @param idCar the id car
	 * @param reservationDTO the reservation DTO
	 * @return the reservation DTO
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@PostMapping(CarController.BASE_URL + "/{idCar}/reservations")
	@ResponseStatus(HttpStatus.CREATED)
	public ReservationDTO reservationCar(@PathVariable Long idCar, @RequestBody ReservationDTO reservationDTO) {
		log.debug("REST request to create reservation car: {}  reservationDTO {}", idCar, reservationDTO);
		
		return reservationService.createNew(idCar, reservationDTO);
		
	}
	
	/**
	 * Reservation car 2.
	 *
	 * @param reservationDTO the reservation DTO
	 * @return the reservation DTO
	 */
	@PostMapping(ReservationController.BASE_URL)
	@ResponseStatus(HttpStatus.CREATED)
	public ReservationDTO reservationCar2(@RequestBody ReservationDTO reservationDTO) {
		log.debug("REST request to create reservation car: {}  reservationDTO {}", 1L, reservationDTO);
		
		return reservationService.createNew(reservationDTO);
		
	}

}
