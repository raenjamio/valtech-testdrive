package com.raenjamio.valtech.testdrive.api.v1.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;

@Mapper(componentModel = "spring", uses = {CarMapper.class})
public interface ReservationMapper {//extends EntityMapper<ReservationDTO, Reservation> {
	ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

	@Mapping(source = "carId", target = "car.id")
	Reservation toEntity(ReservationDTO dto);

	@Mapping(source = "car.id", target = "carId")
	ReservationDTO toDto(Reservation entity);

	List<Reservation> toEntity(List<ReservationDTO> dtoList);

	List<ReservationDTO> toDto(List<Reservation> entityList);

}
