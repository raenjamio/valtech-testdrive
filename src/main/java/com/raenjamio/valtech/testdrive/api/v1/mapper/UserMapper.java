package com.raenjamio.valtech.testdrive.api.v1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.raenjamio.valtech.testdrive.api.v1.domain.User;
import com.raenjamio.valtech.testdrive.api.v1.model.user.UserDTO;

@Mapper(componentModel= "spring", uses= {})
public interface UserMapper extends EntityMapper<UserDTO, User> {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	
	@Mapping(target = "reservations", ignore = true)
    User toEntity(UserDTO userDTO);
}
