package com.raenjamio.valtech.testdrive.api.usecase;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.management.RuntimeErrorException;

import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.springframework.stereotype.Service;

import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.service.CarService;
import com.raenjamio.valtech.testdrive.api.v1.service.ReservationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CreateReservation {
	
	private final CarService carService;
	private final ReservationService reservationService;
	
	public CreateReservation(CarService carService, ReservationService reservationService) {
		super();
		this.carService = carService;
		this.reservationService = reservationService;
	}

	public ReservationDTO create(Long idCar, ReservationDTO reservationDTO) {
		log.debug("create reservation");

		CarDTO car = carService.findById(idCar);
		
		if (car == null ) {
			new RuntimeErrorException(null, " No existe un auto para reservar ");
		}
		log.debug("create reservation car {}", car);

		reservationDTO.setDateArrival(reservationDTO.getDateDeparture().plusDays(1));
		car.addReservation(reservationDTO, car.getId());
		
		return reservationService.createNew(reservationDTO);
	}

	

}
