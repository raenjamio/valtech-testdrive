package com.raenjamio.valtech.testdrive.api.v1.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
import com.raenjamio.valtech.testdrive.api.controller.v1.CarController;
import com.raenjamio.valtech.testdrive.api.v1.domain.Car;
import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation;
import com.raenjamio.valtech.testdrive.api.v1.mapper.CarMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.CarRepository;
import com.raenjamio.valtech.testdrive.api.v1.service.CarService;
import com.raenjamio.valtech.testdrive.api.v1.service.query.CarQueryService;
import com.raenjamio.valtech.testdrive.util.CarTest;
import com.raenjamio.valtech.testdrive.util.TestUtil;

/**
 * Test class for the CarController REST controller.
 *
 * @see CarController
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestDriveApplication.class)
public class CarControllerIntTest {

	private static final String OTHER_DESC = "otherDesc";

	private static final String OTHER_BRAND = "otherBrand";

	@Autowired
	private CarRepository carRepository;

	@Autowired
	private CarMapper carMapper;

	@Autowired
	private CarService carService;

	@Autowired
	private CarQueryService carQueryService;

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

	private MockMvc restCarMockMvc;

	private Car car;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final CarController carResource = new CarController(carQueryService,
				carService);
		this.restCarMockMvc = MockMvcBuilders.standaloneSetup(carResource)
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
	public static Car createEntity(EntityManager em) {
		Car car = CarTest.buildCarTest(1L);
		return car;
	}

	@Before
	public void initTest() {
		car = createEntity(em);
	}

	@Test
	@Transactional
	public void createCar() throws Exception {
		int databaseSizeBeforeCreate = carRepository.findAll().size();
		// Create the Car
		CarDTO carDTO = carMapper.toDto(car);
		MvcResult result = restCarMockMvc
				.perform(post(CarController.BASE_URL + "").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(carDTO)))
				.andExpect(status().isCreated()).andReturn();

		// Validate the Car in the database
		String content = result.getResponse().getContentAsString();
		List<Car> carList = carRepository.findAll();
		assertThat(carList).hasSize(1);
		Car testCar = carList.get(carList.size() - 1);
		assertEquals(testCar.getBrand(), CarTest.BRAND);
		assertEquals(testCar.getDescription(), CarTest.DESCRIPTION);
		assertEquals(testCar.getId(), Long.valueOf(CarTest.ID));
	}


	@Test
	@Transactional
	public void getAllCars() throws Exception {
		// Initialize the database
		Car carSaved = carRepository.saveAndFlush(car);

		// Get all the carList
		restCarMockMvc.perform(get(CarController.BASE_URL + "?sort=id,desc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.data.content.[*].id").value(hasItem(carSaved.getId().intValue())))
				.andExpect(jsonPath("$.data.content.[*].brand").value(hasItem(CarTest.BRAND)))
				.andExpect(jsonPath("$.data.content.[*].description").value(hasItem(CarTest.DESCRIPTION)));
		assertTrue(true);
	}

	@Test
	@Transactional
	public void getCar() throws Exception {
		// Initialize the database
		Car carSaved = carRepository.saveAndFlush(car);

		// Get the car
		restCarMockMvc.perform(get(CarController.BASE_URL + "/{id}", carSaved.getId()))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(carSaved.getId().intValue()))
				.andExpect(jsonPath("$.brand").value(CarTest.BRAND))
				.andExpect(jsonPath("$.description").value(CarTest.DESCRIPTION))

		;
		assertTrue(true);
	}

	@Test
	@Transactional
	public void getAllCarsByCarIdIsEqualToSomething() throws Exception {
		// Initialize the database
		carRepository.saveAndFlush(car);

		defaultCarShouldBeFound("id.equals=" + car.getId().toString());

		// Get all the carList where marca equals to UPDATED_MARCA
		defaultCarShouldNotBeFound("id.equals=" + 100);
		assertTrue(true);
	}

	@Test
	@Transactional
	public void getAllCarsByUserIdIsInShouldWork() throws Exception {
		// Initialize the database
		carRepository.saveAndFlush(car);

		defaultCarShouldBeFound("id.in=" + car.getId().toString());

		defaultCarShouldNotBeFound("id.in= 100");
		assertTrue(true);
	}

	@Test
	@Transactional
	public void getAllCarsByUserIdIsNullOrNotNull() throws Exception {
		// Initialize the database
		carRepository.saveAndFlush(car);

		// Get all the carList where marca is not null
		defaultCarShouldBeFound("id.specified=true");

		// Get all the carList where marca is null
		defaultCarShouldNotBeFound("id.specified=false");
		assertTrue(true);
	}

	/**
	 * Executes the search, and checks that the default entity is returned
	 */
	private void defaultCarShouldBeFound(String filter) throws Exception {
		restCarMockMvc.perform(get(CarController.BASE_URL + "?sort=id,desc&" + filter))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				// .andExpect(jsonPath("$.data.content.[*].id").value(hasItem(carSaved.getId().intValue())))
				.andExpect(jsonPath("$.data.content.[*].brand").value(hasItem(CarTest.BRAND)))
				.andExpect(jsonPath("$.data.content.[*].description").value(hasItem(CarTest.DESCRIPTION)))
				;
	}

	/**
	 * Executes the search, and checks that the default entity is not returned
	 */
	private void defaultCarShouldNotBeFound(String filter) throws Exception {
		restCarMockMvc.perform(get(CarController.BASE_URL + "?sort=id,desc&" + filter))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.data.content").isArray()).andExpect(jsonPath("$.data.content").isEmpty());

	}

	@Test
	@Transactional
	public void getNonExistingCar() throws Exception {
		// Get the car
		restCarMockMvc.perform(get(CarController.BASE_URL + "/{id}", Long.MAX_VALUE))
				.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updateCar() throws Exception {
		// Initialize the database
		Car carSaved = carRepository.saveAndFlush(car);

		int databaseSizeBeforeUpdate = carRepository.findAll().size();

		// Update the car
		Car updatedCar = carRepository.findById(carSaved.getId()).get();
		// Disconnect from session so that the updates on updatedCar are not
		// directly saved in db
		em.detach(updatedCar);
		updatedCar.setBrand(OTHER_BRAND);
		updatedCar.setDescription(OTHER_DESC);
		CarDTO carDTO = carMapper.toDto(updatedCar);

		restCarMockMvc.perform(put(CarController.BASE_URL + "/" + updatedCar.getId())
				.contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(carDTO)))
				.andExpect(status().isOk());

		// Validate the Car in the database
		List<Car> carList = carRepository.findAll();
		assertThat(carList).hasSize(databaseSizeBeforeUpdate);
		Car testCar = carList.get(carList.size() - 1);
		assertThat(testCar.getBrand()).isEqualTo(OTHER_BRAND);
		assertThat(testCar.getDescription()).isEqualTo(OTHER_DESC);
	}

	@Test
	@Transactional
	public void updateNonExistingCar() throws Exception {
		int databaseSizeBeforeUpdate = carRepository.findAll().size();

		// Create the Car
		CarDTO carDTO = carMapper.toDto(car);

		// If the entity doesn't have an ID, it will throw BadRequestAlertException
		restCarMockMvc
				.perform(put(CarController.BASE_URL + "").contentType(TestUtil.APPLICATION_JSON_UTF8)
						.content(TestUtil.convertObjectToJsonBytes(carDTO)))
				.andExpect(status().isMethodNotAllowed());

		// Validate the Car in the database
		List<Car> carList = carRepository.findAll();
		assertThat(carList).hasSize(databaseSizeBeforeUpdate);
	}

	@Test
	@Transactional
	public void deleteCar() throws Exception {
		// Initialize the database
		Car carSaved = carRepository.saveAndFlush(car);

		int databaseSizeBeforeDelete = carRepository.findAll().size();

		// Delete the car
		restCarMockMvc.perform(delete(CarController.BASE_URL + "/{id}", carSaved.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isOk());

		// Validate the database is empty
		List<Car> carList = carRepository.findAll();
		assertThat(carList).hasSize(databaseSizeBeforeDelete - 1);
	}
	
	@Test
	@Transactional
	public void addReservationVerifier() throws Exception {
		Car car1 = new Car();
		car1.setId(1L);
		Reservation reservation = new Reservation();
		reservation.setId(1L);
		car1.addReservation(reservation);
		
		assertTrue(car1.getReservations().contains(reservation));
		assertTrue(reservation.getCar().equals(car1));
	}
	

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(Car.class);
		Car car1 = new Car();
		car1.setId(1L);
		Car car2 = new Car();
		car2.setId(car1.getId());
		assertThat(car1).isEqualTo(car2);
		car2.setId(2L);
		assertThat(car1).isNotEqualTo(car2);
		car1.setId(null);
		assertThat(car1).isNotEqualTo(car2);
	}

	@Test
	@Transactional
	public void dtoEqualsVerifier() throws Exception {
		TestUtil.equalsVerifier(CarDTO.class);
		CarDTO carDTO1 = new CarDTO();
		carDTO1.setId(1L);
		CarDTO carDTO2 = new CarDTO();
		assertThat(carDTO1).isNotEqualTo(carDTO2);
		carDTO2.setId(carDTO1.getId());
		assertThat(carDTO1).isEqualTo(carDTO2);
		carDTO2.setId(2L);
		assertThat(carDTO1).isNotEqualTo(carDTO2);
		carDTO1.setId(null);
		assertThat(carDTO1).isNotEqualTo(carDTO2);
	}

}
