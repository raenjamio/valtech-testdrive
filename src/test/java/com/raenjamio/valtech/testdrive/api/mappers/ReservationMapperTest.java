package com.raenjamio.valtech.testdrive.api.mappers;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation;
import com.raenjamio.valtech.testdrive.api.v1.domain.ReservationState;
import com.raenjamio.valtech.testdrive.api.v1.mapper.ReservationMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.util.ReservationTest;

public class ReservationMapperTest {

	public static final long ID = 1L;

	private static final String EMAIL = "asdf@asdf.com";
	private static final String NAME = "carlos";
	private static final String LAST_NAME = "alvarez";
	private static final LocalDateTime DATE_DEPARTURE =  ReservationTest.DATE_DEPARTURE;
	private static final LocalDateTime DATE_ARRIVAL = ReservationTest.DATE_ARRIVAL;

	ReservationMapper reservationMapper = ReservationMapper.INSTANCE;

	@Test
	public void reservationToReservationDTO() throws Exception {
		// given
		Reservation reservation = new Reservation();

		// TODO agregar atributos del address
		reservation = ReservationTest.buildReservationTest(1L);

		// when
		ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

		// then
		assertEquals(Long.valueOf(ID), reservationDTO.getId());
		assertEquals(reservationDTO.getState(), ReservationState.CREATED);
		assertEquals(reservationDTO.getDateArrival(), ReservationTest.DATE_ARRIVAL);
		assertEquals(reservationDTO.getDateDeparture(), ReservationTest.DATE_DEPARTURE);
		assertEquals(reservationDTO.getCarId(), Long.valueOf(ID));
		assertEquals(reservationDTO.getEmail(), EMAIL);
		assertEquals(reservationDTO.getName(), NAME);
		assertEquals(reservationDTO.getLastName(), LAST_NAME);
	}

	@Test
	public void reservationDtoToReservation() throws Exception {
		// given
		ReservationDTO reservationDTO = new ReservationDTO();

		// TODO agregar atributos del address
		reservationDTO = ReservationTest.builReservationDTOTest(1L);

		// when
		Reservation reservation = reservationMapper.toEntity(reservationDTO);

		// then
		assertEquals(Long.valueOf(ID), reservation.getId());
		assertEquals(reservation.getState(), ReservationState.CREATED);
		assertEquals(reservation.getDateArrival(), ReservationTest.DATE_ARRIVAL);
		assertEquals(reservation.getDateDeparture(), ReservationTest.DATE_DEPARTURE);
		assertEquals(reservation.getCar().getId(), Long.valueOf(ID));
		assertEquals(reservation.getUser().getEmail(), EMAIL);
		assertEquals(reservation.getUser().getName(), NAME);
		assertEquals(reservation.getUser().getLastName(), LAST_NAME);
	}


}
