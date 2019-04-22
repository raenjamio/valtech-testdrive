package com.raenjamio.valtech.testdrive.api.usecase.available;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.raenjamio.valtech.testdrive.api.v1.domain.ReservationState;
import com.raenjamio.valtech.testdrive.api.v1.model.available.AvailableDTO;
import com.raenjamio.valtech.testdrive.api.v1.model.available.PageAvailable;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;
import com.raenjamio.valtech.testdrive.api.v1.service.CarService;
import com.raenjamio.valtech.testdrive.exceptions.BadRequestAlertException;
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

	@Value("${reservation.hourDeparture}")
	private int hourDeparture;
	
	@Value("${availavility.months.default}")
	private int monthsDefault;

	public GetAvailavility(CarService carService) {
		super();
		this.carService = carService;
	}

	public PageAvailable get(Long idCar, String dateStart, String dateEnd) {
		log.debug("get availavility idcar {}", idCar);

		LocalDate startDate = null;
		LocalDate endDate = null;

		DateTimeFormatter dateTimeFormat = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();

		if (dateStart != null) {
			startDate = dateTimeFormat.parse(dateStart, LocalDate::from);
		} else {
			startDate = LocalDate.now();
		}

		// si no se filtra por fecha hasta se pone por default 3 meses desde la fecha
		// desde
		if (dateEnd != null) {
			endDate = dateTimeFormat.parse(dateEnd, LocalDate::from);
		} else {
			endDate = startDate.plusMonths(monthsDefault);
		}

		if (!startDate.isBefore(endDate)) {
			throw new BadRequestAlertException(
					"La fecha hasta es menor a la fecha desde" + startDate + " --- " + endDate, "availavility", "date");
		}

		CarDTO car = carService.findById(idCar);

		
		Set<DateRange> datesReserved = car.getReservations().stream()
				.filter(reservation -> ReservationState.CREATED.equals(reservation.getState()))
				.map( x -> new DateRange(x.getDateDeparture().toLocalDate() , x.getDateArrival().toLocalDate())).collect(Collectors.toSet());
				
		DateRange dateRange = new DateRange(startDate, endDate);

		List<AvailableDTO> availabilities = buildAvailabilities(dateRange, car.getId(), datesReserved);

		return new PageAvailable(availabilities);
	}
	
	private List<AvailableDTO> buildAvailabilities(DateRange dateRange, Long carId, Set<DateRange> datesReservedRange) {
				
		return dateRange.stream()
				.filter(x -> !datesReservedRange.stream().anyMatch(reserved -> inRangeReserved(reserved, x)))
				.map(dateDeparture -> new AvailableDTO(carId, dateDeparture, dateDeparture))
				.collect(Collectors.toList());
	}

	/**
	 * Verificamos si un fecha esta dentro de un periodo de fechas reservadas
	 * @param reserved
	 * @return
	 */	
	private boolean inRangeReserved(DateRange reserved, LocalDate date) {
		return (date.isAfter(reserved.getStartDate())|| date.equals(reserved.getStartDate())) 
				&& (date.isBefore(reserved.getEndDate()));
	}

}
