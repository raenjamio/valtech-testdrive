package com.raenjamio.valtech.testdrive.api.controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.raenjamio.valtech.testdrive.api.usecase.available.GetAvailavility;
import com.raenjamio.valtech.testdrive.api.v1.model.available.PageAvailable;

import lombok.extern.slf4j.Slf4j;

/**
 * Punto de entrada para ver la disponibilidad de un auto
 * Solo se encarga de eso
 * @author raenjamio
 *
 */
@Slf4j
@RestController
public class AvailableController {

	public static final String BASE_URL = "/api/v1/available";

	private final GetAvailavility availavilityUseCase;

	public AvailableController(GetAvailavility availavilityUseCase) {
		super();
		this.availavilityUseCase = availavilityUseCase;
	}

	/**
	 *  
	 * @param idCar es requerido
	 * @param dateStart no es requerida por default toma la fecha del dia
	 * @param dateEnd no es requerida por default toma 3 meses desde dateStart
	 * @return
	 */
	@GetMapping(CarController.BASE_URL + "/{idCar}/available")
	@ResponseStatus(HttpStatus.OK)
	public PageAvailable getAll(@PathVariable Long idCar,
			@RequestParam(value = "dateStart", required = false) String dateStart,
			@RequestParam(value = "dateEnd", required = false) String dateEnd) {// (@PathVariable Long idCar, ReservationCriteria criteria, Pageable pageable) {
		
		log.debug("REST request to get reservations by idCar: {} dateDeparture: {} dateArrival: {}", idCar, dateStart, dateEnd);

		return availavilityUseCase.get(idCar, dateStart, dateEnd);
	}

}
