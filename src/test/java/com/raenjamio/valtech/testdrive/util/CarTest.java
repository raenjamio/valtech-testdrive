package com.raenjamio.valtech.testdrive.util;

import com.raenjamio.valtech.testdrive.api.v1.domain.Car;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;

public class CarTest {
	
	

	public static final String DESCRIPTION = "descriptions";
	public static final String BRAND = "aaaaa";
	public static final long ID = 1L;

	
	
	
	public static Car buildCarTest(long l) {
		Car car = new Car();
		car.setBrand(BRAND);
		car.setDescription(DESCRIPTION);
		car.setId(l);
		return car;
	}
	
	public static CarDTO builCarDTOTest(Long id) {
		CarDTO carDTO = new CarDTO();
		carDTO.setBrand(BRAND);
		carDTO.setDescription(DESCRIPTION);
		carDTO.setId(id);
		return carDTO;
	}

}
