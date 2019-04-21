package com.raenjamio.valtech.testdrive.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.raenjamio.valtech.testdrive.api.v1.domain.User;
import com.raenjamio.valtech.testdrive.api.v1.mapper.UserMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.user.UserDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.UserRepository;
import com.raenjamio.valtech.testdrive.api.v1.service.UserService;
import com.raenjamio.valtech.testdrive.api.v1.service.impl.UserServiceImpl;
import com.raenjamio.valtech.testdrive.util.UserTest;


@ContextConfiguration(classes = UserServiceImplTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackageClasses = {UserMapper.class})
public class UserServiceImplTest {

	private static final String NAME_MODIF = "nameModif";

	private static final String LAST_MODIF = "lastModif";

	@Mock
    UserRepository userRepository;

	@Autowired
    UserMapper userMapper;
	
    UserService userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userService = new UserServiceImpl(userMapper, userRepository, null);
    }

    @Test
    public void getAll() throws Exception {
        //given
        User user1 = UserTest.buildUserTest(1L);
        
        User user2 = UserTest.buildUserTest(2L);
        

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        //when
        Set<UserDTO> userDTOS = userService.findAll();

        //then
        assertEquals(2, userDTOS.size());

    }

    @Test
    public void getById() throws Exception {
        //given
        User user1 = UserTest.buildUserTest(1L);
        
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(user1));

        //when
        UserDTO userDTO = userService.findById(1L);

        assertEquals(UserTest.EMAIL, userDTO.getEmail());
        assertEquals(new Long(UserTest.ID), userDTO.getId());
        assertEquals(UserTest.NAME, userDTO.getName());
        assertEquals(UserTest.LAST_NAME, userDTO.getLastName());
    }
    
    @Test
    public void patch() throws Exception {
        //given
        User user1 = UserTest.buildUserTest(1L);
        
        UserDTO userDTO = UserTest.builUserDTOTest(1L);
        userDTO.setName(NAME_MODIF);
        userDTO.setLastName(LAST_MODIF);
        
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);
        
        //when
        UserDTO savedDto = userService.patch(1L, userDTO);

        assertEquals(savedDto.getEmail(), UserTest.EMAIL);
        assertEquals(savedDto.getName(), NAME_MODIF);
        assertEquals(savedDto.getLastName(), LAST_MODIF);
        assertEquals(new Long(1), savedDto.getId());
    }

    @Test
    public void createNew() throws Exception {

        //given
        UserDTO userDTO = UserTest.builUserDTOTest(1L);

        User savedUser = UserTest.buildUserTest(1L);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        //when
        UserDTO savedDto = userService.createNew(userDTO);

        //then
        assertEquals(UserTest.NAME, savedDto.getName());
        assertEquals(UserTest.LAST_NAME, savedDto.getLastName());
        assertEquals(UserTest.EMAIL, savedDto.getEmail());
        assertEquals(new Long(1), savedDto.getId());
    }

    @Test
    public void saveByDTO() throws Exception {

        //given
        UserDTO userDTO = UserTest.builUserDTOTest(1L);

        User savedUser = UserTest.buildUserTest(1L);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        //when
        UserDTO savedDto = userService.saveByDTO(1L, userDTO);


        assertEquals(UserTest.NAME, savedDto.getName());
        assertEquals(UserTest.LAST_NAME, savedDto.getLastName());
        assertEquals(UserTest.EMAIL, savedDto.getEmail());
        assertEquals(new Long(1), savedDto.getId());
    }

    @Test
    public void deleteUserById() throws Exception {

        Long id = 1L;

        userRepository.deleteById(id);

        verify(userRepository, times(1)).deleteById(anyLong());
    }
    
    @Test
    public void deleteUser() throws Exception {

    	UserDTO userDTO = UserTest.builUserDTOTest(1L);

        userService.delete(userDTO);

        verify(userRepository, times(1)).delete(any());
    }
    
    

}
