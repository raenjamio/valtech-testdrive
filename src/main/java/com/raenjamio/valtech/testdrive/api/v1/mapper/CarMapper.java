package com.raenjamio.valtech.testdrive.api.v1.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.raenjamio.valtech.testdrive.api.v1.domain.Car;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;

@Mapper(componentModel= "spring", uses= {})
public interface CarMapper {
	CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);
	

	CarDTO toDto(Car car);
	
	Car toEntity(CarDTO carDTO);
	
	List<CarDTO> toDto(List<Car> cars);
	
	List<Car> toEntity(List<CarDTO> cars);
	
}
