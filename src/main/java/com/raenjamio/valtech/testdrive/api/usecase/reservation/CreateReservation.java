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
 * Servicio de aplicacion o caso de uso para crear una reserva para un auto
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
	private  BigDecimal priceReservation;
	
	public CreateReservation(CarService carService, UserService userService) {
		super();
		this.carService = carService;
		this.userService = userService;
	}

	public synchronized ReservationDTO create(ReservationDTO reservationDTO) {
		log.debug("create reservation");

		CarDTO car = carService.findById(reservationDTO.getCarId());
		
		if (car == null ) {
			throw new BadRequestAlertException(" No existe un auto para reservar ","reservation", "car.id");
		}
		if (reservationDTO.getDateDeparture() == null) {
			throw new BadRequestAlertException(" Falta la fecha de reserva (dateDeparture)", "reservation", "dateDeparture");
		}
		
		if (reservationDTO.getName() == null || reservationDTO.getLastName() == null) {
			throw new BadRequestAlertException(" El nombre y el apellido son obligatirios", "client", "name");
		}
		
		Optional<UserDTO> user = userService.findByNameAndLastNameEquals(reservationDTO.getName(), reservationDTO.getLastName());
		
		// si el usuario no existe lo creo
		if (!user.isPresent()) {
			UserDTO userDTO = new UserDTO(reservationDTO.getName(), reservationDTO.getLastName(), reservationDTO.getEmail());
			userDTO = userService.createNew(userDTO);
			reservationDTO.setUserId(userDTO.getId());
		} else {
			reservationDTO.setUserId(user.get().getId());
		}
			
		
		log.debug("create reservation car {}", car);

		LocalDateTime dateDepartureCalculed = reservationDTO.getDateDeparture().withHour(hourDeparture).withMinute(minuteDeparture);
		
		reservationDTO.setDateDeparture(dateDepartureCalculed);
		
		LocalDateTime dateArrivalCalculed = dateDepartureCalculed.plusDays(maxDayReservation).withHour(hourArrival).withMinute(minuteArrival);
		
		reservationDTO.setDateArrival(dateArrivalCalculed);
		reservationDTO.setPrice(priceReservation);
		
		Set<ReservationDTO> reservationFilteredByDates = car.getReservations().stream()
			.filter(reservation -> reservation.getDateArrival().isAfter(reservationDTO.getDateDeparture()))
			.filter(reservation -> reservation.getDateDeparture().isBefore(reservationDTO.getDateArrival()))
			.filter(reservation -> ReservationState.CREATED.equals(reservation.getState()))
			.collect(Collectors.toSet());
		
		//si existe una reserva dentro del periodo no se puede realizar la reserva
		if (!reservationFilteredByDates.isEmpty() && !reservationFilteredByDates.contains(reservationDTO)) {
			//throw new BadRequestAlertException("Existe una reserva en el periodo indicado", "reservations", ErrorConstants.ERR_VALIDATION);
			throw new BadRequestAlertException("Existe una reserva en el periodo indicado", "reservation", "dateDeparture");
		}
		//validamos fecha maxima y minima de reserva
		LocalDateTime dateMinReservation = LocalDateTime.now().minusWeeks(minWeekReservation);
		LocalDateTime dateMaxReservation = LocalDateTime.now().minusMonths(maxMonthReservation);
		
		if (reservationDTO.getDateDeparture().isBefore(dateMinReservation)) {
			throw new BadRequestAlertException("La fecha de reserva es menor a la fecha minima de reserva " + dateMinReservation, "reservation", "dateDeparture");
		}
		
		if (reservationDTO.getDateDeparture().isAfter(dateMaxReservation)) {
			throw new BadRequestAlertException("La fecha de reserva es mayor a la fecha maxima de reserva " + dateMaxReservation, "reservation", "dateDeparture");
		}
		
		car.addReservation(reservationDTO, car.getId());
		

		
		return reservationDTO;
	}

	

}
