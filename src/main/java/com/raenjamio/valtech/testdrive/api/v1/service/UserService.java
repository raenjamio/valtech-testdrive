package com.raenjamio.valtech.testdrive.api.v1.service;

import java.util.Optional;

import com.raenjamio.valtech.testdrive.api.v1.model.user.UserDTO;

public interface UserService extends CrudService<UserDTO, Long>{


	UserDTO createNew(UserDTO userDTO);
	
	UserDTO saveByDTO(Long id, UserDTO userDTO);

	UserDTO patch(Long id, UserDTO userDTO);
	
	Optional<UserDTO> findByNameAndLastNameEquals(String name, String lastName);

}
