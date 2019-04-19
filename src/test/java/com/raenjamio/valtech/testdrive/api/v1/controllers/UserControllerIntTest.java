package com.raenjamio.valtech.testdrive.api.v1.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import com.raenjamio.valtech.testdrive.TestDriveApplication;
import com.raenjamio.valtech.testdrive.api.controller.v1.UserController;
import com.raenjamio.valtech.testdrive.api.v1.domain.User;
import com.raenjamio.valtech.testdrive.api.v1.mapper.UserMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.user.UserDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.UserRepository;
import com.raenjamio.valtech.testdrive.api.v1.service.UserService;
import com.raenjamio.valtech.testdrive.api.v1.service.query.UserQueryService;
import com.raenjamio.valtech.testdrive.util.TestUtil;
import com.raenjamio.valtech.testdrive.util.UserTest;

/**
 * Test class for the UserController REST controller.
 *
 * @see UserController
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestDriveApplication.class)
public class UserControllerIntTest {

	private static final String OTHER_NAME = "otherDesc";

	private static final String OTHER_EMAIL = "otherBrand@gmail.com";

	private static final String OTHER_LAST_NAME = "otherLast";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private UserQueryService userQueryService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	// @Autowired
	// private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	@Autowired
	private Validator validator;

	private MockMvc restUserMockMvc;

	private User user;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final UserController userResource = new UserController(userQueryService,
				userService);
		this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				// .setControllerAdvice(exceptionTranslator)
				// .setConversionService(createFormattingConversionService())
				.setMessageConverters(jacksonMessageConverter).setValidator(validator).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it, if
	 * they test an entity which requires the current entity.
	 */
	public static User createEntity(EntityManager em) {
		User user = UserTest.buildUserTest(1L);
		return user;
	}

	@Before
	public void initTest() {
		user = createEntity(em);
	}

	@Test
	@Transactional
	public void createUser() throws Exception {
		int databaseSizeBeforeCreate = userRepository.findAll().size();
		// Create the User
		UserDTO userDTO = userMapper.toDto(user);
		MvcResult result = restUserMockMvc
				.perform(post(UserController.BASE_URL + "").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(userDTO)))
				.andExpect(status().isCreated()).andReturn();

		// Validate the User in the database
		String content = result.getResponse().getContentAsString();
		List<User> userList = userRepository.findAll();
		assertThat(userList).hasSize(1);
		User testUser = userList.get(userList.size() - 1);
		assertEquals(testUser.getName(), UserTest.NAME);
		assertEquals(testUser.getLastName(), UserTest.LAST_NAME);
		assertEquals(testUser.getEmail(), UserTest.EMAIL);
		//assertEquals(testUser.getId(), Long.valueOf(2));
	}


	@Test
	@Transactional
	public void getAllUsers() throws Exception {
		// Initialize the database
		User userSaved = userRepository.saveAndFlush(user);

		// Get all the userList
		restUserMockMvc.perform(get(UserController.BASE_URL + "?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.data.content.[*].id").value(hasItem(userSaved.getId().intValue())))
				.andExpect(jsonPath("$.data.content.[*].name").value(hasItem(UserTest.NAME)))
				.andExpect(jsonPath("$.data.content.[*].lastName").value(hasItem(UserTest.LAST_NAME)))
				.andExpect(jsonPath("$.data.content.[*].email").value(hasItem(UserTest.EMAIL)));
	}

	@Test
	@Transactional
	public void getUser() throws Exception {
		// Initialize the database
		User userSaved = userRepository.saveAndFlush(user);

		// Get the user
		restUserMockMvc.perform(get(UserController.BASE_URL + "/{id}", userSaved.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(userSaved.getId().intValue()))
				.andExpect(jsonPath("$.name").value(UserTest.NAME))
				.andExpect(jsonPath("$.lastName").value(UserTest.LAST_NAME))
				.andExpect(jsonPath("$.email").value(UserTest.EMAIL))

		;
	}

	@Test
	@Transactional
	public void getAllUsersByUserIdIsEqualToSomething() throws Exception {
		// Initialize the database
		user = userRepository.saveAndFlush(user);

		defaultUserShouldBeFound("id.equals=" + user.getId());

		// Get all the userList where marca equals to UPDATED_MARCA
		defaultUserShouldNotBeFound("id.equals=" + 100);
	}
	
	@Test
	@Transactional
	public void getAllUsersByNameIdIsEqualToSomething() throws Exception {
		// Initialize the database
		userRepository.saveAndFlush(user);

		defaultUserShouldBeFound("name.equals=" + user.getName());

		// Get all the userList where marca equals to UPDATED_MARCA
		defaultUserShouldNotBeFound("name.equals=" + "z");
	}
	
	@Test
	@Transactional
	public void getAllUsersByLastNameIdIsEqualToSomething() throws Exception {
		// Initialize the database
		userRepository.saveAndFlush(user);

		defaultUserShouldBeFound("lastName.equals=" + user.getLastName());

		// Get all the userList where marca equals to UPDATED_MARCA
		defaultUserShouldNotBeFound("lastName.equals=" + "z");
	}
	
	@Test
	@Transactional
	public void getAllUsersByEmailIdIsEqualToSomething() throws Exception {
		// Initialize the database
		userRepository.saveAndFlush(user);

		defaultUserShouldBeFound("email.equals=" + user.getEmail());

		// Get all the userList where marca equals to UPDATED_MARCA
		defaultUserShouldNotBeFound("email.equals=" + "z");
	}

	@Test
	@Transactional
	public void getAllUsersByUserIdIsInShouldWork() throws Exception {
		// Initialize the database
		user = userRepository.saveAndFlush(user);

		defaultUserShouldBeFound("id.in=" + user.getId());

		defaultUserShouldNotBeFound("id.in= 100");
	}

	@Test
	@Transactional
	public void getAllUsersByUserIdIsNullOrNotNull() throws Exception {
		// Initialize the database
		userRepository.saveAndFlush(user);

		// Get all the userList where marca is not null
		defaultUserShouldBeFound("id.specified=true");

		// Get all the userList where marca is null
		defaultUserShouldNotBeFound("id.specified=false");
	}

	/**
	 * Executes the search, and checks that the default entity is returned
	 */
	private void defaultUserShouldBeFound(String filter) throws Exception {
		restUserMockMvc.perform(get(UserController.BASE_URL + "?sort=id,desc&" + filter))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				// .andExpect(jsonPath("$.data.content.[*].id").value(hasItem(userSaved.getId().intValue())))
				.andExpect(jsonPath("$.data.content.[*].name").value(hasItem(UserTest.NAME)))
				.andExpect(jsonPath("$.data.content.[*].lastName").value(hasItem(UserTest.LAST_NAME)))
				.andExpect(jsonPath("$.data.content.[*].email").value(hasItem(UserTest.EMAIL)));
				;
	}

	/**
	 * Executes the search, and checks that the default entity is not returned
	 */
	private void defaultUserShouldNotBeFound(String filter) throws Exception {
		restUserMockMvc.perform(get(UserController.BASE_URL + "?sort=id,desc&" + filter))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.data.content").isArray()).andExpect(jsonPath("$.data.content").isEmpty());

	}

	@Test
	@Transactional
	public void getNonExistingUser() throws Exception {
		// Get the user
		restUserMockMvc.perform(get(UserController.BASE_URL + "/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateUser() throws Exception {
		// Initialize the database
		User userSaved = userRepository.saveAndFlush(user);

		int databaseSizeBeforeUpdate = userRepository.findAll().size();

		// Update the user
		User updatedUser = userRepository.findById(userSaved.getId()).get();
		// Disconnect from session so that the updates on updatedUser are not
		// directly saved in db
		em.detach(updatedUser);
		updatedUser.setEmail(OTHER_EMAIL);
		updatedUser.setName(OTHER_NAME);
		updatedUser.setLastName(OTHER_LAST_NAME);
		UserDTO userDTO = userMapper.toDto(updatedUser);

		restUserMockMvc.perform(put(UserController.BASE_URL + "/" + updatedUser.getId())
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(userDTO)))
				.andExpect(status().isOk());

		// Validate the User in the database
		List<User> userList = userRepository.findAll();
		assertThat(userList).hasSize(databaseSizeBeforeUpdate);
		User testUser = userList.get(userList.size() - 1);
		assertThat(testUser.getEmail()).isEqualTo(OTHER_EMAIL);
		assertThat(testUser.getName()).isEqualTo(OTHER_NAME);
		assertThat(testUser.getLastName()).isEqualTo(OTHER_LAST_NAME);
	}

	@Test
	@Transactional
	public void updateNonExistingUser() throws Exception {
		int databaseSizeBeforeUpdate = userRepository.findAll().size();

		// Create the User
		UserDTO userDTO = userMapper.toDto(user);

		// If the entity doesn't have an ID, it will throw BadRequestAlertException
		restUserMockMvc
				.perform(put(UserController.BASE_URL + "").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(userDTO)))
				.andExpect(status().isMethodNotAllowed());

		// Validate the User in the database
		List<User> userList = userRepository.findAll();
		assertThat(userList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	public void deleteUser() throws Exception {
		// Initialize the database
		User userSaved = userRepository.saveAndFlush(user);

		int databaseSizeBeforeDelete = userRepository.findAll().size();

		// Delete the user
		restUserMockMvc.perform(delete(UserController.BASE_URL + "/{id}", userSaved.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

		// Validate the database is empty
		List<User> userList = userRepository.findAll();
		assertThat(userList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(User.class);
		User user1 = new User();
		user1.setId(1L);
		User user2 = new User();
		user2.setId(user1.getId());
		assertThat(user1).isEqualTo(user2);
		user2.setId(2L);
		assertThat(user1).isNotEqualTo(user2);
		user1.setId(null);
		assertThat(user1).isNotEqualTo(user2);
	}

	@Test
	@Transactional
	public void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(UserDTO.class);
		UserDTO userDTO1 = new UserDTO();
		userDTO1.setId(1L);
		UserDTO userDTO2 = new UserDTO();
		assertThat(userDTO1).isNotEqualTo(userDTO2);
		userDTO2.setId(userDTO1.getId());
		assertThat(userDTO1).isEqualTo(userDTO2);
		userDTO2.setId(2L);
		assertThat(userDTO1).isNotEqualTo(userDTO2);
		userDTO1.setId(null);
		assertThat(userDTO1).isNotEqualTo(userDTO2);
	}

}
