package com.raenjamio.valtech.testdrive.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.raenjamio.valtech.testdrive.api.v1.domain.Car;
import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation;
import com.raenjamio.valtech.testdrive.api.v1.domain.ReservationState;
import com.raenjamio.valtech.testdrive.api.v1.domain.User;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;

public class ReservationTest {
	
	

	public static final long ID = 1L;

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private static String dateArrival = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)).plusDays(11).format(formatter); // "2019-02-18 18:00";
	private static String dateDeparture = LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0)).plusDays(10).format(formatter); //"2019-02-18 10:00";
	
	public static final String EMAIL = "asdf@asdf.com";
	public static final String NAME = "carlos";
	public static final String LAST_NAME = "alvarez";
	public static final LocalDateTime DATE_DEPARTURE =  LocalDateTime.parse(dateDeparture, formatter);
	public static final LocalDateTime DATE_ARRIVAL =  LocalDateTime.parse(dateArrival, formatter);
	public static final BigDecimal PRICE = new BigDecimal("200");
	
	
	public static Reservation buildReservationTest(long l) {
		Reservation reservation = new Reservation();
		reservation.setPrice(PRICE);
		reservation.setId(l);
		reservation.setState(ReservationState.CREATED);
		reservation.setDateArrival(DATE_ARRIVAL);
		reservation.setDateDeparture(DATE_DEPARTURE);
		reservation.setCar(Car.builder().id(ID).build());
		reservation.setUser(User.builder().email(EMAIL).name(NAME).lastName(LAST_NAME).build());
		return reservation;
	}
	
	public static ReservationDTO builReservationDTOTest(Long id) {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setPrice(PRICE);
		reservationDTO.setId(id);
		reservationDTO.setState(ReservationState.CREATED);
		reservationDTO.setDateArrival(DATE_ARRIVAL);
		reservationDTO.setDateDeparture(DATE_DEPARTURE);
		reservationDTO.setCarId(ID);
		reservationDTO.setName(NAME);
		reservationDTO.setLastName(LAST_NAME);
		reservationDTO.setEmail(EMAIL);
		return reservationDTO;
	}

}
