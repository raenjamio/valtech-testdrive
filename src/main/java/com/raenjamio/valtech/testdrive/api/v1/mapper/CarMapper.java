package com.raenjamio.valtech.testdrive.api.v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.raenjamio.valtech.testdrive.api.v1.domain.Car;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;

@Mapper(componentModel= "spring", uses= {})
public interface CarMapper extends EntityMapper<CarDTO, Car> {
	CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);
	
	@Mapping(target = "reservations", ignore = true)
    Car toEntity(CarDTO carDTO);
}
