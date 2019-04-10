package com.raenjamio.valtech.testdrive.api.v1.service;

import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;

public interface CarService extends CrudService<CarDTO, Long>{


	CarDTO createNew(CarDTO userDTO);
	
	CarDTO saveByDTO(Long id, CarDTO userDTO);

	CarDTO patch(Long id, CarDTO userDTO);

}
