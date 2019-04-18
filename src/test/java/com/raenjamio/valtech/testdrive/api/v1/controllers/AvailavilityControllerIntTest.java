package com.raenjamio.valtech.testdrive.api.v1.controllers;


import static org.assertj.core.api.Assertions.not;
import static org.assertj.core.api.Assertions.notIn;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import com.raenjamio.valtech.testdrive.TestDriveApplication;
import com.raenjamio.valtech.testdrive.api.controller.v1.AvailableController;
import com.raenjamio.valtech.testdrive.api.controller.v1.CarController;
import com.raenjamio.valtech.testdrive.api.controller.v1.ReservationController;
import com.raenjamio.valtech.testdrive.api.usecase.available.GetAvailavility;
import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation;
import com.raenjamio.valtech.testdrive.api.v1.domain.ReservationState;
import com.raenjamio.valtech.testdrive.api.v1.mapper.ReservationMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.ReservationRepository;
import com.raenjamio.valtech.testdrive.api.v1.repository.UserRepository;
import com.raenjamio.valtech.testdrive.api.v1.service.ReservationService;
import com.raenjamio.valtech.testdrive.util.ReservationTest;

/**
 * Test class for the ReservationController REST controller.
 *
 * @see ReservationController
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestDriveApplication.class)
public class AvailavilityControllerIntTest {

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
    private GetAvailavility availavilityUseCase;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatterResponse  = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    //@Autowired
    //private ExceptionTranslator exceptionTranslator;
    static LocalDate dateStart = LocalDate.now().plusDays(20);
    LocalDate dateEnd = dateStart.plusDays(10);
    
    String dateStartResponse = dateStart.atTime(18, 0, 0).format(formatterResponse);
    String dateEndResponse = dateEnd.atTime(10, 0, 0).format(formatterResponse);

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restReservationMockMvc;

    private Reservation reservation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AvailableController resource = new AvailableController(availavilityUseCase);
        this.restReservationMockMvc = MockMvcBuilders.standaloneSetup(resource)
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
        reservation.setDateDeparture(dateStart.atTime(18, 0, 0).plusDays(5));
        //reservation.setUser(User.builder().email("adfad").name("").build());
        return reservation;
    }

    @Before
    public void initTest() {
        reservation = createEntity(em);
    }

    /**
     * Verificamos que se retorne una lista de available
     * @throws Exception
     */
    @Test
    @Transactional
    public void getAllReservationsOK() throws Exception {
        // Initialize the database
    	userRepository.save(reservation.getUser());
		// Get all the reservationList
        restReservationMockMvc.perform(get(CarController.BASE_URL + "/1/available?dateStart=" + dateStart.format(formatter)  + "&dateEnd=" + dateEnd.format(formatter) ))
            .andExpect(status().isOk())	
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.data.[*].dateDeparture").isArray())
            .andExpect(jsonPath("$.data.[*].dateDeparture").value(hasItem(dateStartResponse)))
            .andExpect(jsonPath("$.data.[*].dateArrival").isArray())
            .andExpect(jsonPath("$.data.[*].dateArrival").value(hasItem(dateEndResponse)))
            .andExpect(jsonPath("$.data.[*].carUrl").value(hasItem(CarController.BASE_URL + "/1")))
            .andExpect(jsonPath("$.data.[*].reservationUrl").value(hasItem(CarController.BASE_URL + "/1/reservations")))
            ;
    }
    
    

    /**
     * Verificamos que si existe una reserva esta no se muestra como disponible
     * @throws Exception
     */
    @Test
    @Transactional
    public void getAllReservationsWithoutReservation() throws Exception {
        // Initialize the database
    	userRepository.saveAndFlush(reservation.getUser());
    	
        Reservation reservationSaved = reservationRepository.saveAndFlush(reservation);
        
        String dateReservation = reservationSaved.getDateDeparture().format(formatterResponse);

		// Get all the reservationList
        restReservationMockMvc.perform(get(CarController.BASE_URL + "/1/available?dateStart=" + dateStart.format(formatter)  + "&dateEnd=" + dateEnd.format(formatter) ))
            .andExpect(status().isOk())	
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.data.[*].dateDeparture").isArray())
            .andExpect(jsonPath("$.data.[*].dateDeparture").value(hasItem(dateStartResponse)))
           //.andExpect(jsonPath("$.data.[*].dateDeparture").value(hasItem(dateReservation)))
            .andExpect(jsonPath("$.data.[*].dateArrival").isArray())
            .andExpect(jsonPath("$.data.[*].dateArrival").value(hasItem(dateEndResponse)))
            .andExpect(jsonPath("$.data.[*].carUrl").value(hasItem(CarController.BASE_URL + "/1")))
            .andExpect(jsonPath("$.data.[*].reservationUrl").value(hasItem(CarController.BASE_URL + "/1/reservations")))
            ;
    }
    
    
    
    /**
     * Verificamos que si no enviamos la fecha hasta muestra la disponibilidad de 3 meses
     * @throws Exception
     */
    @Test
    @Transactional
    public void getAllReservationsWithoutDateEnd() throws Exception {
        // Initialize the database
    	userRepository.saveAndFlush(reservation.getUser());
    	
    	String dateStartEnd = dateStart.atTime(18, 0, 0).plusMonths(3).format(formatterResponse);
    	
        
		// Get all the reservationList
        restReservationMockMvc.perform(get(CarController.BASE_URL + "/1/available?dateStart=" + dateStart.format(formatter)))
            .andExpect(status().isOk())	
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.data.[*].dateDeparture").isArray())
            .andExpect(jsonPath("$.data.[*].dateDeparture").value(hasItem(dateStartResponse)))
            .andExpect(jsonPath("$.data.[*].dateDeparture").value(hasItem(dateStartEnd)))
            .andExpect(jsonPath("$.data.[*].dateArrival").isArray())
            .andExpect(jsonPath("$.data.[*].dateArrival").value(hasItem(dateEndResponse)))
            .andExpect(jsonPath("$.data.[*].carUrl").value(hasItem(CarController.BASE_URL + "/1")))
            .andExpect(jsonPath("$.data.[*].reservationUrl").value(hasItem(CarController.BASE_URL + "/1/reservations")))
            ;
    }

}
