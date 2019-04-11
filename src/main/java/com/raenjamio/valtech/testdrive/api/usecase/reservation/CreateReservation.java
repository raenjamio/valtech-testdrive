package com.raenjamio.valtech.testdrive.api.usecase.reservation;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.service.CarService;
import com.raenjamio.valtech.testdrive.api.v1.service.ReservationService;

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
	private final ReservationService reservationService;
	
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
	
	public CreateReservation(CarService carService, ReservationService reservationService) {
		super();
		this.carService = carService;
		this.reservationService = reservationService;
	}

	public ReservationDTO create(Long idCar, ReservationDTO reservationDTO) {
		log.debug("create reservation");

		CarDTO car = carService.findById(idCar);
		
		if (car == null ) {
			throw new RuntimeErrorException(null, " No existe un auto para reservar ");
		}
		
		
		
		log.debug("create reservation car {}", car);

		LocalDateTime dateDepartureCalculed = reservationDTO.getDateDeparture().withHour(hourDeparture).withMinute(minuteDeparture);
		
		reservationDTO.setDateDeparture(dateDepartureCalculed);
		
		LocalDateTime dateArrivalCalculed = dateDepartureCalculed.plusDays(maxDayReservation).withHour(hourArrival).withMinute(minuteArrival);
		
		reservationDTO.setDateArrival(dateArrivalCalculed);
		
		Set<ReservationDTO> reservationFilteredByDates = car.getReservations().stream()
			.filter(reservation -> reservation.getDateArrival().isAfter(reservationDTO.getDateDeparture()))
			.filter(reservation -> reservation.getDateDeparture().isBefore(reservationDTO.getDateArrival()))
			.collect(Collectors.toSet());
		
		//si existe una reserva dentro del periodo no se puede realizar la reserva
		if (!reservationFilteredByDates.isEmpty()) {
			//throw new BadRequestAlertException("Existe una reserva en el periodo indicado", "reservations", ErrorConstants.ERR_VALIDATION);
			throw new RuntimeErrorException(null, "Existe una reserva en el periodo indicado");
		}
		//validamos fecha maxima y minima de reserva
		LocalDateTime dateMinReservation = LocalDateTime.now().minusWeeks(-1);
		LocalDateTime dateMaxReservation = LocalDateTime.now().minusMonths(-1);
		
		if (reservationDTO.getDateDeparture().isBefore(dateMinReservation)) {
			throw new RuntimeErrorException(null, "La fecha de reserva es menor a la fecha minima de reserva " + dateMinReservation);
		}
		
		if (reservationDTO.getDateDeparture().isAfter(dateMaxReservation)) {
			throw new RuntimeErrorException(null, "La fecha de reserva es mayor a la fecha maxima de reserva " + dateMaxReservation);
		}
		
		car.addReservation(reservationDTO, car.getId());
		

		
		return reservationService.createNew(reservationDTO);
	}

	

}
