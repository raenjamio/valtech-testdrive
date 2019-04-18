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

	private final GetAvailavility availavilityUseCase;

	public AvailableController(GetAvailavility availavilityUseCase) {
		super();
		this.availavilityUseCase = availavilityUseCase;
	}

	@GetMapping(CarController.BASE_URL + "/{idCar}/available")
	@ResponseStatus(HttpStatus.OK)
	public PageAvailable getAll(@PathVariable Long idCar,
			@RequestParam(value = "dateStart", required = false) String dateStart,
			@RequestParam(value = "dateEnd", required = false) String dateEnd) {// (@PathVariable Long idCar, ReservationCriteria criteria, Pageable pageable) {
		
		log.debug("REST request to get reservations by idCar: {} dateDeparture: {} dateArrival: {}", idCar, dateStart, dateEnd);

		return availavilityUseCase.get(idCar, dateStart, dateEnd);
	}

}
