package com.raenjamio.valtech.testdrive.api.controller.v1;

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

import com.raenjamio.valtech.testdrive.api.usecase.reservation.CreateReservation;
import com.raenjamio.valtech.testdrive.api.v1.criteria.ReservationCriteria;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.PageReservation;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.service.CarService;
import com.raenjamio.valtech.testdrive.api.v1.service.ReservationService;
import com.raenjamio.valtech.testdrive.api.v1.service.query.ReservationQueryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
//RequestMapping(ReservationController.BASE_URL)
public class ReservationController {
	
	public static final String BASE_URL = "/api/v1/reservations";
	
	private final ReservationQueryService reservationQueryService;
	private final ReservationService reservationService;
	private final CreateReservation createReservation;
	
	
	public ReservationController(ReservationQueryService reservationQueryService,
			ReservationService reservationService, CreateReservation createReservation) {
		super();
		this.reservationQueryService = reservationQueryService;
		this.reservationService = reservationService;
		this.createReservation = createReservation;
	}

	@GetMapping(ReservationController.BASE_URL)
	@ResponseStatus(HttpStatus.OK)
	public PageReservation getAll(ReservationCriteria criteria, Pageable pageable) {
		log.debug("REST request to get reservations by criteria: {}", criteria);
		Page<ReservationDTO> page = reservationQueryService.findByCriteria(criteria, pageable);
		return new PageReservation(page);
	}
	
	@DeleteMapping( ReservationController.BASE_URL + "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		log.debug("REST request to delete reservation : {}", id);
		reservationService.deleteById(id);
	}

	@PutMapping(ReservationController.BASE_URL + "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ReservationDTO update(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
		log.debug("REST request to update reservation id: {} reservation {}", id, reservationDTO);
		return reservationService.saveByDTO(id, reservationDTO);
	}

	@PatchMapping(ReservationController.BASE_URL + "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ReservationDTO patch(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
		log.debug("REST request to patch reservation id: {} reservation {}", id, reservationDTO);
		return reservationService.patch(id, reservationDTO);
	}
	
	@PostMapping(CarController.BASE_URL + "/{idCar}/reservations")
	@ResponseStatus(HttpStatus.CREATED)
	public ReservationDTO reservationCar(@PathVariable Long idCar, @RequestBody ReservationDTO reservationDTO) {
		log.debug("REST request to create reservation car: {}  reservationDTO {}", idCar, reservationDTO);
		
		return createReservation.create(idCar, reservationDTO);
		
	}

}
