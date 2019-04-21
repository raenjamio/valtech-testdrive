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
import com.raenjamio.valtech.testdrive.api.controller.v1.ReservationController;
import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation;
import com.raenjamio.valtech.testdrive.api.v1.domain.ReservationState;
import com.raenjamio.valtech.testdrive.api.v1.mapper.ReservationMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.ReservationRepository;
import com.raenjamio.valtech.testdrive.api.v1.repository.UserRepository;
import com.raenjamio.valtech.testdrive.api.v1.service.ReservationService;
import com.raenjamio.valtech.testdrive.api.v1.service.query.ReservationQueryService;
import com.raenjamio.valtech.testdrive.util.ReservationTest;
import com.raenjamio.valtech.testdrive.util.TestUtil;

/**
 * Test class for the ReservationController REST controller.
 *
 * @see ReservationController
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestDriveApplication.class)
public class ReservationControllerIntTest {

    private static final ReservationState DEFAULT_STATE = ReservationState.CREATED;
    private static final ReservationState UPDATED_STATE = ReservationState.CANCELED;

    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationQueryService reservationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    //@Autowired
    //private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restReservationMockMvc;

    private Reservation reservation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReservationController reservationResource = new ReservationController(reservationQueryService, reservationService);
        this.restReservationMockMvc = MockMvcBuilders.standaloneSetup(reservationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            //.setControllerAdvice(exceptionTranslator)
            //.setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createEntity(EntityManager em) {
        Reservation reservation = ReservationTest.buildReservationTest(1L);
        //reservation.setUser(User.builder().email("adfad").name("").build());
        return reservation;
    }

    @Before
    public void initTest() {
        reservation = createEntity(em);
    }

    @Test
    @Transactional
    public void createReservation() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();
        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);
        MvcResult result = restReservationMockMvc.perform(post(ReservationController.BASE_URL + "")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isCreated())
            .andReturn();

        // Validate the Reservation in the database
        String content = result.getResponse().getContentAsString();
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate + 1);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getState()).isEqualTo(DEFAULT_STATE);
		assertEquals(testReservation.getDateArrival(), ReservationTest.DATE_ARRIVAL);
		assertEquals(testReservation.getDateDeparture(), ReservationTest.DATE_DEPARTURE);
		assertEquals(testReservation.getCar().getId(), Long.valueOf(ReservationTest.ID));
		assertEquals(testReservation.getUser().getEmail(), ReservationTest.EMAIL);
		assertEquals(testReservation.getUser().getName(), ReservationTest.NAME);
		assertEquals(testReservation.getUser().getLastName(), ReservationTest.LAST_NAME);
    }

	@Test
    @Transactional
    public void createReservationWithoutDateDeparture() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // Create the Reservation with an existing ID
        reservation.setId(1L);
        reservation.setDateArrival(null);
        reservation.setDateDeparture(null);
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationMockMvc.perform(post(ReservationController.BASE_URL + "")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate);
    }
	
	
	@Test
    @Transactional
    public void createReservationWithoutName() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // Create the Reservation with an existing ID
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);
        reservationDTO.setName(null);
        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationMockMvc.perform(post(ReservationController.BASE_URL + "")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllReservations() throws Exception {
        // Initialize the database
    	userRepository.save(reservation.getUser());
        //reservationRepository.saveAndFlush(reservation);
        Reservation reservationSaved = reservationRepository.save(reservation);

        // Get all the reservationList
        restReservationMockMvc.perform(get(ReservationController.BASE_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.data.content.[*].id").value(hasItem(reservationSaved.getId().intValue())))
            .andExpect(jsonPath("$.data.content.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.data.content.[*].name").value(hasItem(ReservationTest.NAME)))
            .andExpect(jsonPath("$.data.content.[*].lastName").value(hasItem(ReservationTest.LAST_NAME)))
            .andExpect(jsonPath("$.data.content.[*].email").value(hasItem(ReservationTest.EMAIL)))
            .andExpect(jsonPath("$.data.content.[*].dateArrival").value(hasItem(ReservationTest.DATE_ARRIVAL.toString().replaceAll("T", " "))))
            .andExpect(jsonPath("$.data.content.[*].dateDeparture").value(hasItem(ReservationTest.DATE_DEPARTURE.toString().replaceAll("T", " "))))
            ;
    }
    
    @Test
    @Transactional
    public void getReservation() throws Exception {
        // Initialize the database
    	userRepository.save(reservation.getUser());
    	Reservation reservationSaved = reservationRepository.save(reservation);

        // Get the reservation
        restReservationMockMvc.perform(get(ReservationController.BASE_URL + "/{id}", reservationSaved.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reservationSaved.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.name").value(ReservationTest.NAME))
            .andExpect(jsonPath("$.lastName").value(ReservationTest.LAST_NAME))
            .andExpect(jsonPath("$.email").value(ReservationTest.EMAIL))
            .andExpect(jsonPath("$.dateArrival").value(ReservationTest.DATE_ARRIVAL.toString().replaceAll("T", " ")))
            .andExpect(jsonPath("$.dateDeparture").value(ReservationTest.DATE_DEPARTURE.toString().replaceAll("T", " ")))

            ;
    }

    @Test
    @Transactional
    public void getAllReservationsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
    	userRepository.saveAndFlush(reservation.getUser());
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where marca equals to DEFAULT_MARCA
        defaultReservationShouldBeFound("userId.equals=" + reservation.getUser().getId().toString());

        // Get all the reservationList where marca equals to UPDATED_MARCA
        defaultReservationShouldNotBeFound("userId.equals=" + 100);
        
    }
    
    @Test
    @Transactional
    public void getAllReservationsByCarIdIsEqualToSomething() throws Exception {
        // Initialize the database
    	userRepository.saveAndFlush(reservation.getUser());
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where marca equals to DEFAULT_MARCA
        defaultReservationShouldBeFound("carId.equals=" + reservation.getCar().getId());

        // Get all the reservationList where marca equals to UPDATED_MARCA
        defaultReservationShouldNotBeFound("carId.equals=" + 100);
    }

    @Test
    @Transactional
    public void getAllReservationsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
    	userRepository.saveAndFlush(reservation.getUser());
        reservationRepository.saveAndFlush(reservation);

        defaultReservationShouldBeFound("userId.in=" + reservation.getUser().getId().toString());

        defaultReservationShouldNotBeFound("userId.in= 100");
    }

    @Test
    @Transactional
    public void getAllReservationsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
    	userRepository.saveAndFlush(reservation.getUser());
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where marca is not null
        defaultReservationShouldBeFound("userId.specified=true");

        // Get all the reservationList where marca is null
        defaultReservationShouldNotBeFound("userId.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultReservationShouldBeFound(String filter) throws Exception {
        restReservationMockMvc.perform(get(ReservationController.BASE_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            //.andExpect(jsonPath("$.data.content.[*].id").value(hasItem(reservationSaved.getId().intValue())))
            .andExpect(jsonPath("$.data.content.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.data.content.[*].name").value(hasItem(ReservationTest.NAME)))
            .andExpect(jsonPath("$.data.content.[*].lastName").value(hasItem(ReservationTest.LAST_NAME)))
            .andExpect(jsonPath("$.data.content.[*].email").value(hasItem(ReservationTest.EMAIL)))
            ;

    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultReservationShouldNotBeFound(String filter) throws Exception {
        restReservationMockMvc.perform(get(ReservationController.BASE_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.data.content").isArray())
            .andExpect(jsonPath("$.data.content").isEmpty());

    }


    @Test
    @Transactional
    public void getNonExistingReservation() throws Exception {
        // Get the reservation
        restReservationMockMvc.perform(get(ReservationController.BASE_URL + "/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservation() throws Exception {
        // Initialize the database
    	userRepository.saveAndFlush(reservation.getUser());
        Reservation reservationSaved = reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation
        Reservation updatedReservation = reservationRepository.findById(reservationSaved.getId()).get();
        // Disconnect from session so that the updates on updatedReservation are not directly saved in db
        em.detach(updatedReservation);
        updatedReservation.setState(UPDATED_STATE);
        ReservationDTO reservationDTO = reservationMapper.toDto(updatedReservation);

        restReservationMockMvc.perform(put(ReservationController.BASE_URL + "/" + updatedReservation.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Create the Reservation
        ReservationDTO reservationDTO = reservationMapper.toDto(reservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc.perform(put(ReservationController.BASE_URL + "")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReservation() throws Exception {
        // Initialize the database
    	userRepository.saveAndFlush(reservation.getUser());
        Reservation reservationSaved = reservationRepository.saveAndFlush(reservation);

        int databaseSizeBeforeDelete = reservationRepository.findAll().size();

        // Delete the reservation
        restReservationMockMvc.perform(delete(ReservationController.BASE_URL + "/{id}", reservationSaved.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservation.class);
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        Reservation reservation2 = new Reservation();
        reservation2.setId(reservation1.getId());
        assertThat(reservation1).isEqualTo(reservation2);
        reservation2.setId(2L);
        assertThat(reservation1).isNotEqualTo(reservation2);
        reservation1.setId(null);
        assertThat(reservation1).isNotEqualTo(reservation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservationDTO.class);
        ReservationDTO reservationDTO1 = new ReservationDTO();
        reservationDTO1.setId(1L);
        ReservationDTO reservationDTO2 = new ReservationDTO();
        assertThat(reservationDTO1).isNotEqualTo(reservationDTO2);
        reservationDTO2.setId(reservationDTO1.getId());
        assertThat(reservationDTO1).isEqualTo(reservationDTO2);
        reservationDTO2.setId(2L);
        assertThat(reservationDTO1).isNotEqualTo(reservationDTO2);
        reservationDTO1.setId(null);
        assertThat(reservationDTO1).isNotEqualTo(reservationDTO2);
    }

}
