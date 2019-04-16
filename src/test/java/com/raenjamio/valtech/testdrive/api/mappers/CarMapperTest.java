package com.raenjamio.valtech.testdrive.api.mappers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.raenjamio.valtech.testdrive.api.v1.domain.Car;
import com.raenjamio.valtech.testdrive.api.v1.mapper.CarMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;
import com.raenjamio.valtech.testdrive.util.CarTest;

public class CarMapperTest {

	public static final long ID = 1L;

	CarMapper carMapper = CarMapper.INSTANCE;

	@Test
	public void carToCarDTO() throws Exception {
		// given
		Car car = new Car();

		car = CarTest.buildCarTest(1L);

		// when
		CarDTO carDTO = carMapper.toDto(car);

		// then
		assertEquals(carDTO.getId(), Long.valueOf(ID));
		assertEquals(carDTO.getDescription(), CarTest.DESCRIPTION);
		assertEquals(carDTO.getBrand(), CarTest.BRAND);
	}

	@Test
	public void carDtoToCar() throws Exception {
		// given
		CarDTO carDTO = new CarDTO();

		// TODO agregar atributos del address
		carDTO = CarTest.builCarDTOTest(1L);

		// when
		Car car = carMapper.toEntity(carDTO);
		// then
		assertEquals(car.getId(), Long.valueOf(ID));
		assertEquals(car.getDescription(), CarTest.DESCRIPTION);
		assertEquals(car.getBrand(), CarTest.BRAND);
	}


}
