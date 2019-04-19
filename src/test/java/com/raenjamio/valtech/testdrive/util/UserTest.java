package com.raenjamio.valtech.testdrive.util;

import com.raenjamio.valtech.testdrive.api.v1.domain.User;
import com.raenjamio.valtech.testdrive.api.v1.model.user.UserDTO;

public class UserTest {
	
	

	public static final String NAME = "name";
	public static final String LAST_NAME = "lastname";
	public static final String EMAIL = "email@gmail.com";
	public static final long ID = 1L;

	
	
	
	public static User buildUserTest(long l) {
		User user = new User();
		user.setName(NAME);
		user.setLastName(LAST_NAME);
		user.setEmail(EMAIL);
		user.setId(l);
		return user;
	}
	
	public static UserDTO builUserDTOTest(Long id) {
		UserDTO userDTO = new UserDTO();
		userDTO.setName(NAME);
		userDTO.setLastName(LAST_NAME);
		userDTO.setEmail(EMAIL);
		userDTO.setId(id);
		return userDTO;
	}

}
