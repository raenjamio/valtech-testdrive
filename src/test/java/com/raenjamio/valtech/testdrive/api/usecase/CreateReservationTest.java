package com.raenjamio.valtech.testdrive.api.usecase;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.raenjamio.valtech.testdrive.api.usecase.reservation.CreateReservation;
import com.raenjamio.valtech.testdrive.api.v1.mapper.ReservationMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.model.user.UserDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.ReservationRepository;
import com.raenjamio.valtech.testdrive.api.v1.service.CarService;
import com.raenjamio.valtech.testdrive.api.v1.service.UserService;
import com.raenjamio.valtech.testdrive.exceptions.BadRequestAlertException;
import com.raenjamio.valtech.testdrive.util.CarTest;
import com.raenjamio.valtech.testdrive.util.ReservationTest;
import com.raenjamio.valtech.testdrive.util.UserTest;

@ContextConfiguration(classes = CreateReservationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackageClasses = { ReservationMapper.class })
public class CreateReservationTest {

	@Mock
	ReservationRepository reservationRepository;

	@Mock
	UserService userService;

	@Mock
	CarService carService;

	CreateReservation createReservation;

	private ReservationDTO reservationDTO;

	private CarDTO car;

	private UserDTO user;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		createReservation = new CreateReservation(carService, userService);
		reservationDTO = ReservationTest.builReservationDTOTest(1L);
		car = CarTest.builCarDTOTest(1L);
		user = UserTest.builUserDTOTest(1L);

		ReflectionTestUtils.setField(createReservation, "hourDeparture", 18);
		ReflectionTestUtils.setField(createReservation, "hourArrival", 10);
		ReflectionTestUtils.setField(createReservation, "minuteDeparture", 0);
		ReflectionTestUtils.setField(createReservation, "minuteArrival", 0);
		ReflectionTestUtils.setField(createReservation, "maxDayReservation", 1);
		ReflectionTestUtils.setField(createReservation, "minWeekReservation", -1);
		ReflectionTestUtils.setField(createReservation, "maxMonthReservation", -1);
	}

	/**
	 * Test para verificar que se puede crear una reserva si existe el usuario,
	 * y la reserva queda asociada a dicho usuario
	 * 
	 * @throws Exception
	 */

	@Test
	public void createOKWithUser() throws Exception {
		// given

		when(carService.findById(anyLong())).thenReturn(car);
		when(userService.findByNameAndLastNameEquals(any(), any())).thenReturn(Optional.of(user));

		// when
		ReservationDTO reservationCreated = createReservation.create(reservationDTO);

		// then
		assertEquals(car.getId(), reservationCreated.getCarId());
		assertEquals(ReservationTest.DATE_ARRIVAL, reservationCreated.getDateArrival());
		assertEquals(ReservationTest.DATE_DEPARTURE, reservationCreated.getDateDeparture());
		assertEquals(user.getEmail(), reservationCreated.getEmail());
		assertEquals(user.getName(), reservationCreated.getName());
		assertEquals(user.getLastName(), reservationCreated.getLastName());
		assertEquals(user.getLastName(), reservationCreated.getLastName());
		
		verify(userService, never()).createNew(any());

	}

	/**
	 * Test para verificar que se puede crear una reserva si no existe el usuario,
	 * ya que este se crea
	 * 
	 * @throws Exception
	 */

	@Test
	public void createOkWithoutUser() throws Exception {
		// given

		when(carService.findById(anyLong())).thenReturn(car);
		when(userService.findByNameAndLastNameEquals(any(), any())).thenReturn(Optional.empty());
		when(userService.createNew(any())).thenReturn(user);

		// when
		ReservationDTO reservationCreated = createReservation.create(reservationDTO);

		// then
		assertEquals(car.getId(), reservationCreated.getCarId());
		assertEquals(ReservationTest.DATE_ARRIVAL, reservationCreated.getDateArrival());
		assertEquals(ReservationTest.DATE_DEPARTURE, reservationCreated.getDateDeparture());
		assertEquals(user.getEmail(), reservationCreated.getEmail());
		assertEquals(user.getName(), reservationCreated.getName());
		assertEquals(user.getLastName(), reservationCreated.getLastName());
		assertEquals(user.getLastName(), reservationCreated.getLastName());
		
		verify(userService, Mockito.times(1)).createNew(any());

	}

	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	/**
	 * 
	 * Test para verificar que no se puede crear una reserva si no se encuentra un
	 * auto asociado al id
	 * 
	 * @throws Exception
	 */

	@Test // (expected = BadRequestAlertException.class)
	public void createNoOkWithoutCar() throws Exception {

		exceptionRule.expect(BadRequestAlertException.class);
		exceptionRule.expectMessage(" No existe un auto para reservar ");

		when(carService.findById(anyLong())).thenReturn(null);
		when(userService.findByNameAndLastNameEquals(any(), any())).thenReturn(Optional.empty());
		when(userService.createNew(any())).thenReturn(user);

		// when
		createReservation.create(reservationDTO);

	}

	/**
	 * Test para verificar que no se puede crear una reserva si la fecha de retiro
	 * del auto no existe
	 * 
	 * @throws Exception
	 */

	@Test // (expected = BadRequestAlertException.class)
	public void createNotOkWithoutDateDeparture() throws Exception {

		exceptionRule.expect(BadRequestAlertException.class);
		exceptionRule.expectMessage(" Falta la fecha de reserva (dateDeparture)");

		when(carService.findById(anyLong())).thenReturn(car);
		when(userService.findByNameAndLastNameEquals(any(), any())).thenReturn(Optional.empty());
		when(userService.createNew(any())).thenReturn(user);

		reservationDTO.setDateDeparture(null);

		// when
		createReservation.create(reservationDTO);

	}

	/**
	 * Test para verificar que no se puede crear una reserva si la fecha de retiro
	 * del auto es menor al tiempo configurado
	 * 
	 * @throws Exception
	 */
	@Test
	public void createNotOkBecauseDateDepartureIsLess() throws Exception {

		exceptionRule.expect(BadRequestAlertException.class);
		exceptionRule.expectMessage("La fecha de reserva es menor a la fecha minima de reserva ");

		when(carService.findById(anyLong())).thenReturn(car);
		when(userService.findByNameAndLastNameEquals(any(), any())).thenReturn(Optional.empty());
		when(userService.createNew(any())).thenReturn(user);

		reservationDTO.setDateDeparture(LocalDateTime.now());

		// when
		createReservation.create(reservationDTO);

	}

	/**
	 * Test para verificar que no se puede crear una reserva si la fecha de retiro
	 * del auto es mayor al tiempo configurado
	 * 
	 * @throws Exception
	 */

	@Test
	public void createNotOkBecauseDateDepartureIsHigh() throws Exception {

		exceptionRule.expect(BadRequestAlertException.class);
		exceptionRule.expectMessage("La fecha de reserva es mayor a la fecha maxima de reserva ");

		when(carService.findById(anyLong())).thenReturn(car);
		when(userService.findByNameAndLastNameEquals(any(), any())).thenReturn(Optional.empty());
		when(userService.createNew(any())).thenReturn(user);

		reservationDTO.setDateDeparture(LocalDateTime.of(2025, 1, 1, 0, 0));

		// when
		createReservation.create(reservationDTO);

	}

	/**
	 * Test para verificar que no se puedan insertar dos reservas con la misma fecha
	 * para el mismo auto
	 * 
	 * @throws Exception
	 */

	@Test
	public void createNotOkBecauseExistReservationInSameTime() throws Exception {

		exceptionRule.expect(BadRequestAlertException.class);
		exceptionRule.expectMessage("Existe una reserva en el periodo indicado");

		CarDTO mockCar = mock(CarDTO.class);

		Set reservations = new HashSet<ReservationDTO>(Arrays.asList(ReservationTest.builReservationDTOTest(99L)));
		when(mockCar.getReservations()).thenReturn(reservations);

		when(carService.findById(anyLong())).thenReturn(mockCar);
		when(userService.findByNameAndLastNameEquals(any(), any())).thenReturn(Optional.empty());
		when(userService.createNew(any())).thenReturn(user);


		// when
		createReservation.create(reservationDTO);

	}

}
