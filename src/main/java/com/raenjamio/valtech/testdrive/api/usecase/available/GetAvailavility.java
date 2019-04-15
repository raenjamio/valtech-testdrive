package com.raenjamio.valtech.testdrive.api.usecase.available;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.raenjamio.valtech.testdrive.api.v1.domain.ReservationState;
import com.raenjamio.valtech.testdrive.api.v1.model.available.AvailableDTO;
import com.raenjamio.valtech.testdrive.api.v1.model.available.PageAvailable;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.service.CarService;
import com.raenjamio.valtech.testdrive.api.v1.service.ReservationService;
import com.raenjamio.valtech.testdrive.api.v1.service.query.ReservationQueryService;
import com.raenjamio.valtech.testdrive.util.DateRange;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author raenjamio
 *
 *         Servicio de aplicacion o caso de uso para devolver las fechas
 *         disponibles por auto
 */

@Service
@Slf4j
public class GetAvailavility {

	private final CarService carService;
	private final ReservationService reservationService;
	private final ReservationQueryService reservationQueryService;

	@Value("${reservation.hourDeparture}")
	private int hourDeparture;

	public GetAvailavility(CarService carService, ReservationService reservationService,
			ReservationQueryService reservationQueryService) {
		super();
		this.carService = carService;
		this.reservationService = reservationService;
		this.reservationQueryService = reservationQueryService;
	}

	public PageAvailable get(Long idCar, String dateDeparture, String dateArrival) {
		log.debug("get availavility idcar {}", idCar);

		LocalDate startDate = null;
		LocalDate endDate = null;

		DateTimeFormatter dateTimeFormat = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();

		if (dateDeparture != null) {
			startDate = dateTimeFormat.parse(dateDeparture, LocalDate::from);
		} else {
			startDate = LocalDate.now();
		}

		// si no se filtra por fecha hasta se pone por default 3 meses desde la fecha
		// desde
		if (dateArrival != null) {
			endDate = dateTimeFormat.parse(dateArrival, LocalDate::from);
		} else {
			endDate = startDate.plusMonths(3);
		}

		if (!startDate.isBefore(endDate)) {
			throw new RuntimeErrorException(null,
					"La fecha hasta es menor a la fecha desde" + startDate + " --- " + endDate);
		}

		CarDTO car = carService.findById(idCar);

		Set<LocalDate> datesReserved = car.getReservations().stream()
				.filter(reservation -> ReservationState.CREATED.equals(reservation.getState()))
				.map(ReservationDTO::getDateDeparture).map(LocalDateTime::toLocalDate).collect(Collectors.toSet());


		DateRange dateRange = new DateRange(startDate, endDate);
		/*dateRange.stream().filter(x -> !datesReserved.contains(x)).collect(Collectors.toList())
				.forEach(System.out::println);*/

		List<AvailableDTO> availabilities = buildAvailabilities(dateRange, car.getId(), datesReserved);

		return new PageAvailable(availabilities);
	}

	private List<AvailableDTO> buildAvailabilities(DateRange dateRange, Long carId, Set<LocalDate> datesReserved) {
		// Stream.iterate(new AvailableDTO(), d ->
		// d.se).limit(ChronoUnit.DAYS.between(startDate, endDate) + 1);

		return dateRange.stream()
				.filter(x -> !datesReserved.contains(x))
				.map(dateDeparture -> new AvailableDTO(carId, dateDeparture, dateDeparture))
				.collect(Collectors.toList());
	}

}
