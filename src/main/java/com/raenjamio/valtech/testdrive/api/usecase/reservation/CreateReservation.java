package com.raenjamio.valtech.testdrive.api.usecase.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.raenjamio.valtech.testdrive.api.v1.domain.ReservationState;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.model.user.UserDTO;
import com.raenjamio.valtech.testdrive.api.v1.service.CarService;
import com.raenjamio.valtech.testdrive.api.v1.service.UserService;
import com.raenjamio.valtech.testdrive.exceptions.BadRequestAlertException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author raenjamio
 *
 *         Servicio de aplicacion o caso de uso para crear una reserva para un
 *         auto
 */

@Service
@Slf4j
public class CreateReservation {

	private final CarService carService;
	private final UserService userService;

	@Value("${reservation.hourDeparture}")
	private int hourDeparture;

	@Value("${reservation.hourArrival}")
	private int hourArrival;

	@Value("${reservation.minuteDeparture}")
	private int minuteDeparture;

	@Value("${reservation.minuteArrival}")
	private int minuteArrival;

	@Value("${reservation.maxDayReservation}")
	private int maxDayReservation;

	@Value("${reservation.minWeekReservation}")
	private int minWeekReservation;

	@Value("${reservation.maxMonthReservation}")
	private int maxMonthReservation;

	@Value("${reservation.price}")
	private BigDecimal priceReservation;

	public CreateReservation(CarService carService, UserService userService) {
		super();
		this.carService = carService;
		this.userService = userService;
	}

	/**
	 * Metdo que valida la creacion de una reserva reserva 
	 * 
	 * @param reservationDTO
	 * @return
	 */
	
	public synchronized ReservationDTO create(ReservationDTO reservationDTO) {
		log.debug("create reservation");

		CarDTO car = carService.findById(reservationDTO.getCarId());

		if (car == null) {
			throw new BadRequestAlertException(" Not exist car for reservation", "reservation", "car.id");
		}
		if (reservationDTO.getDateDeparture() == null) {
			throw new BadRequestAlertException(" Date departure is required", "reservation", "dateDeparture");
		}

		if (reservationDTO.getName() == null || reservationDTO.getLastName() == null) {
			throw new BadRequestAlertException(" Name and lastname are required", "client", "name");
		}

		// buscamos al usuario por nombre y apellido
		Optional<UserDTO> user = userService.findByNameAndLastNameEquals(reservationDTO.getName(),
				reservationDTO.getLastName());

		// si el usuario no existe lo creo
		if (!user.isPresent()) {
			UserDTO userDTO = new UserDTO(reservationDTO.getName(), reservationDTO.getLastName(),
					reservationDTO.getEmail());
			userDTO = userService.createNew(userDTO);
			reservationDTO.setUserId(userDTO.getId());
			reservationDTO.setName(userDTO.getName());
			reservationDTO.setLastName(userDTO.getLastName());
			reservationDTO.setEmail(userDTO.getEmail());
		} else {
			reservationDTO.setUserId(user.get().getId());
			reservationDTO.setName(user.get().getName());
			reservationDTO.setLastName(user.get().getLastName());
			reservationDTO.setEmail(user.get().getEmail());
		}

		log.debug("create reservation car {}", car);

		LocalDateTime dateDepartureCalculed = reservationDTO.getDateDeparture().withHour(hourDeparture)
				.withMinute(minuteDeparture);

		reservationDTO.setDateDeparture(dateDepartureCalculed);

		LocalDateTime dateArrivalCalculed = dateDepartureCalculed.plusDays(maxDayReservation).withHour(hourArrival)
				.withMinute(minuteArrival);

		reservationDTO.setDateArrival(dateArrivalCalculed);
		reservationDTO.setPrice(priceReservation);

		Set<ReservationDTO> reservationFilteredByDates = getReservationsByCarFiltered(reservationDTO, car);

		// si existe una reserva dentro del periodo no se puede realizar la reserva
		if (!reservationFilteredByDates.isEmpty() && !reservationFilteredByDates.contains(reservationDTO)) {
			// throw new BadRequestAlertException("Existe una reserva en el periodo
			// indicado", "reservations", ErrorConstants.ERR_VALIDATION);
			throw new BadRequestAlertException("Exist a reservation in this period", "reservation", "dateDeparture");
		}
		// validamos fecha maxima y minima de reserva
		LocalDateTime dateMinReservation = LocalDateTime.now().minusWeeks(minWeekReservation);
		LocalDateTime dateMaxReservation = LocalDateTime.now().minusMonths(maxMonthReservation);

		if (reservationDTO.getDateDeparture().isBefore(dateMinReservation)) {
			throw new BadRequestAlertException(
					"The reservation date is less than the minimum reservation date " + dateMinReservation,
					"reservation", "dateDeparture");
		}

		if (reservationDTO.getDateDeparture().isAfter(dateMaxReservation)) {
			throw new BadRequestAlertException(
					"The reservation date is longer than the maximum reservation date " + dateMaxReservation, "reservation",
					"dateDeparture");
		}

		car.addReservation(reservationDTO, car.getId());

		return reservationDTO;
	}

	private Set<ReservationDTO> getReservationsByCarFiltered(ReservationDTO reservationDTO, CarDTO car) {
		return car.getReservations().stream()
				.filter(reservation -> reservation.getDateArrival().isAfter(reservationDTO.getDateDeparture()))
				.filter(reservation -> reservation.getDateDeparture().isBefore(reservationDTO.getDateArrival()))
				.filter(reservation -> ReservationState.CREATED.equals(reservation.getState()))
				.collect(Collectors.toSet());
	}

}
