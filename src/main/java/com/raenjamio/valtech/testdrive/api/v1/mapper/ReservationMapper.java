package com.raenjamio.valtech.testdrive.api.v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;

@Mapper(componentModel= "spring", uses= {})
public interface ReservationMapper extends EntityMapper <ReservationDTO, Reservation> {
	ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);
	
	
}
