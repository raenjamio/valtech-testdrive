package com.raenjamio.valtech.testdrive.util;

import com.raenjamio.valtech.testdrive.api.v1.domain.User;
import com.raenjamio.valtech.testdrive.api.v1.model.user.UserDTO;

public class UserTest {
	
	

	public static final String DESCRIPTION = "descriptions";
	public static final String BRAND = "aaaaa";
	public static final long ID = 1L;

	
	
	
	public static User buildUserTest(long l) {
		User user = new User();
		user.setName("name");
		user.setLastName("lastName");
		user.setEmail("email");
		user.setId(l);
		return user;
	}
	
	public static UserDTO builUserDTOTest(Long id) {
		UserDTO userDTO = new UserDTO();
		userDTO.setName("name");
		userDTO.setLastName("lastName");
		userDTO.setEmail("email");
		userDTO.setId(id);
		return userDTO;
	}

}
