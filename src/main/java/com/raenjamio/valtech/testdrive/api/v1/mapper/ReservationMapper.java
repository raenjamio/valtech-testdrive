package com.raenjamio.valtech.testdrive.api.v1.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;

@Mapper(componentModel = "spring", uses = {CarMapper.class})
public interface ReservationMapper {//extends EntityMapper<ReservationDTO, Reservation> {
	ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

	@Mappings({
		@Mapping(source = "carId", target = "car.id"),
		@Mapping(source = "userId", target = "user.id"),
		@Mapping(source = "lastName", target = "user.lastName"),
		@Mapping(source = "name", target = "user.name"),
		@Mapping(source = "email", target = "user.email")
	})
	Reservation toEntity(ReservationDTO dto);

	@Mappings({
		@Mapping(source = "car.id", target = "carId"),
		@Mapping(source = "user.id", target = "userId"),
		@Mapping(source = "user.name", target = "name"),
		@Mapping(source = "user.lastName", target = "lastName"),
		@Mapping(source = "user.email", target = "email")
	})
	ReservationDTO toDto(Reservation entity);

	List<Reservation> toEntity(List<ReservationDTO> dtoList);

	List<ReservationDTO> toDto(List<Reservation> entityList);

}
