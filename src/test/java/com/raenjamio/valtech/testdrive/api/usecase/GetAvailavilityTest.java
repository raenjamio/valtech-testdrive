package com.raenjamio.valtech.testdrive.api.usecase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.raenjamio.valtech.testdrive.api.usecase.available.GetAvailavility;
import com.raenjamio.valtech.testdrive.api.v1.domain.ReservationState;
import com.raenjamio.valtech.testdrive.api.v1.mapper.ReservationMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.available.PageAvailable;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.service.CarService;
import com.raenjamio.valtech.testdrive.exceptions.BadRequestAlertException;
import com.raenjamio.valtech.testdrive.util.CarTest;

@ContextConfiguration(classes = GetAvailavilityTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackageClasses = { ReservationMapper.class })
public class GetAvailavilityTest {

	@Mock
	CarService carService;

	GetAvailavility getAvailavility;

	private CarDTO car;

	private static DateTimeFormatter formatterFilter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private static String dateStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0)).format(formatter); // "2019-02-18
																												// 18:00";
	private static String dateEnd = LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0)).plusDays(90)
			.format(formatter); // "2019-02-18 10:00";

	private static String dateStartFilter = LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0))
			.format(formatterFilter); // "2019-02-18 18:00";
	private static String dateEndFilter = LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0)).plusDays(90)
			.format(formatterFilter); // "2019-02-18 10:00";

	public static final LocalDateTime DATE_START = LocalDateTime.parse(dateStart, formatter);
	public static final LocalDateTime DATE_END = LocalDateTime.parse(dateEnd, formatter);

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		getAvailavility = new GetAvailavility(carService);

		car = CarTest.builCarDTOTest(1L);

		ReflectionTestUtils.setField(getAvailavility, "hourDeparture", 18);
	}

	/**
	 * Test para verificar que si no existe una reserva en el periodo indicado trae
	 * todas las fechas disponibles
	 * 
	 * @throws Exception
	 */

	@Test
	public void getAvailavilityOK() throws Exception {
		// given

		when(carService.findById(anyLong())).thenReturn(car);

		// when
		PageAvailable datesAvailables = getAvailavility.get(car.getId(), dateStartFilter, dateEndFilter);

		// then
		assertEquals(datesAvailables.getData().get(0).getDateDeparture(), DATE_START);
		assertEquals(datesAvailables.getData().get(datesAvailables.getData().size() - 1).getDateDeparture(), DATE_END);

	}

	/**
	 * Test para verificar que si no se pone la fecha hasta trae la disponibilidad
	 * de los proximos 3 meses
	 * 
	 * @throws Exception
	 */

	@Test
	public void getAvailavilityOkWithoutDateEnd() throws Exception {
		// given

		when(carService.findById(anyLong())).thenReturn(car);

		// when
		PageAvailable datesAvailables = getAvailavility.get(car.getId(), dateStartFilter, null);

		// then
		assertEquals(datesAvailables.getData().get(0).getDateDeparture(), DATE_START);
		assertEquals(datesAvailables.getData().get(datesAvailables.getData().size() - 1).getDateDeparture(),
				DATE_START.plusMonths(3));

	}

	/**
	 * Test para verificar que si existe una reserva dentro del periodo de busqueda
	 * no trae la fecha como disponible
	 * 
	 * @throws Exception
	 */

	@Test
	public void getAvailavilityWithoutDateReservation() throws Exception {
		// given
		
		CarDTO mockCar = mock(CarDTO.class);
		ReservationDTO reservationInUse = mock(ReservationDTO.class);

		Set reservations = new HashSet<ReservationDTO>(Arrays.asList(reservationInUse));
		when(mockCar.getReservations()).thenReturn(reservations);
		when(reservationInUse.getState()).thenReturn(ReservationState.CREATED);
		when(reservationInUse.getDateDeparture()).thenReturn(DATE_START.plusDays(10));

		when(carService.findById(anyLong())).thenReturn(mockCar);

		// when
		PageAvailable datesAvailables = getAvailavility.get(car.getId(), dateStartFilter, null);
		Boolean exist = datesAvailables.getData().stream().map(x -> x.getDateDeparture()).anyMatch(x -> x.equals(DATE_START.plusDays(10)));
		// then
		assertEquals(datesAvailables.getData().get(0).getDateDeparture(), DATE_START);
		assertEquals(datesAvailables.getData().get(datesAvailables.getData().size() - 1).getDateDeparture(),
				DATE_START.plusMonths(3));
		assertFalse(exist);
		  

	}
	
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	/**
	 * Test para verificar que se validan las fechas desde y hasta
	 * 
	 * @throws Exception
	 */

	
	@Test // (expected = BadRequestAlertException.class)
	public void getAvailavilityWrongDates() throws Exception {
		
		CarDTO mockCar = mock(CarDTO.class);

		exceptionRule.expect(BadRequestAlertException.class);
		exceptionRule.expectMessage("La fecha hasta es menor a la fecha desde");

		when(carService.findById(anyLong())).thenReturn(mockCar);

		// when
		getAvailavility.get(car.getId(), dateEndFilter, dateStartFilter);

	}

}
