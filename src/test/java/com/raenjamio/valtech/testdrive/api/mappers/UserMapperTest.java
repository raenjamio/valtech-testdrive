package com.raenjamio.valtech.testdrive.api.mappers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.raenjamio.valtech.testdrive.api.v1.domain.User;
import com.raenjamio.valtech.testdrive.api.v1.mapper.UserMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.user.UserDTO;
import com.raenjamio.valtech.testdrive.util.UserTest;

public class UserMapperTest {

	public static final long ID = 1L;

	UserMapper userMapper = UserMapper.INSTANCE;

	@Test
	public void userToUserDTO() throws Exception {
		// given
		User user = new User();

		user = UserTest.buildUserTest(1L);

		// when
		UserDTO userDTO = userMapper.toDto(user);

		// then
		assertEquals(userDTO.getId(), Long.valueOf(ID));
		assertEquals(userDTO.getName(), UserTest.NAME);
		assertEquals(userDTO.getLastName(), UserTest.LAST_NAME);
		assertEquals(userDTO.getEmail(), UserTest.EMAIL);
	}

	@Test
	public void userDtoToUser() throws Exception {
		// given
		UserDTO userDTO = new UserDTO();

		// TODO agregar atributos del address
		userDTO = UserTest.builUserDTOTest(1L);

		// when
		User user = userMapper.toEntity(userDTO);
		// then
		assertEquals(user.getId(), Long.valueOf(ID));
		assertEquals(user.getName(), UserTest.NAME);
		assertEquals(user.getLastName(), UserTest.LAST_NAME);
		assertEquals(user.getEmail(), UserTest.EMAIL);
	}


}
