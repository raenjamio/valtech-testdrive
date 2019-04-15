package com.raenjamio.valtech.testdrive.api.controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.raenjamio.valtech.testdrive.api.usecase.available.GetAvailavility;
import com.raenjamio.valtech.testdrive.api.v1.model.available.PageAvailable;
import com.raenjamio.valtech.testdrive.api.v1.service.ReservationService;
import com.raenjamio.valtech.testdrive.api.v1.service.query.ReservationQueryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
//RequestMapping(ReservationController.BASE_URL)
public class AvailableController {

	public static final String BASE_URL = "/api/v1/available";

	private final ReservationQueryService reservationQueryService;
	private final ReservationService reservationService;
	private final GetAvailavility availavilityUseCase;

	public AvailableController(ReservationQueryService reservationQueryService, ReservationService reservationService,
			GetAvailavility availavilityUseCase) {
		super();
		this.reservationQueryService = reservationQueryService;
		this.reservationService = reservationService;
		this.availavilityUseCase = availavilityUseCase;
	}

	@GetMapping(CarController.BASE_URL + "/{idCar}/available")
	@ResponseStatus(HttpStatus.OK)
	public PageAvailable getAll(@PathVariable Long idCar,
			@RequestParam(value = "dateDeparture", required = false) String dateDeparture,
			@RequestParam(value = "dateArrival", required = false) String dateArrival) {// (@PathVariable Long idCar, ReservationCriteria criteria, Pageable pageable) {
		
		log.debug("REST request to get reservations by idCar: {} dateDeparture: {} dateArrival: {}", idCar, dateDeparture, dateArrival);

		return availavilityUseCase.get(idCar, dateDeparture, dateArrival);
	}

}
