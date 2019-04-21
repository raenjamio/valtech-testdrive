package com.raenjamio.valtech.testdrive.api.v1.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.raenjamio.valtech.testdrive.api.controller.v1.ReservationController;
import com.raenjamio.valtech.testdrive.api.v1.criteria.ReservationCriteria;
import com.raenjamio.valtech.testdrive.api.v1.domain.ReservationState;
import com.raenjamio.valtech.testdrive.api.v1.mapper.ReservationMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.ReservationRepository;
import com.raenjamio.valtech.testdrive.api.v1.service.ReservationService;
import com.raenjamio.valtech.testdrive.api.v1.service.query.ReservationQueryService;
import com.raenjamio.valtech.testdrive.exceptions.CustomRestExceptionHandler;
import com.raenjamio.valtech.testdrive.exceptions.NotFoundException;
import com.raenjamio.valtech.testdrive.util.AbstractRestControllerTest;
import com.raenjamio.valtech.testdrive.util.ReservationTest;

@WebMvcTest
@EnableSpringDataWebSupport
@ComponentScan(basePackageClasses = {ReservationMapper.class})
@ContextConfiguration(classes = ReservationMapper.class)
//@RunWith(SpringJUnit4ClassRunner.class)
public class ReservationControllerTest extends AbstractRestControllerTest{
	
	public static final String NAME = "Jim";

	@Mock
	ReservationService reservationService;
	
	@Mock
	ReservationQueryService reservationQueryService;
	
	@Mock
    MappingJackson2HttpMessageConverter jacksonMessageConverter;

	
	@Mock
    PageableHandlerMethodArgumentResolver pageableArgumentResolver;
	
	@Mock
    ReservationRepository reservationRepository;
	
	@InjectMocks
	ReservationController reservationController;
	
	@InjectMocks
	private ReservationCriteria criteria;
	
	@Mock
	private ReservationMapper reservationMapper;
	
	@Mock
	private Pageable pageable;

    @Mock
    private Validator validator;
	
	MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
	    MockitoAnnotations.initMocks(this);
	
	    mockMvc = MockMvcBuilders.standaloneSetup(new ReservationController(reservationQueryService, reservationService))
	            .setControllerAdvice(new CustomRestExceptionHandler())
	            .setCustomArgumentResolvers(pageableArgumentResolver)
	            //.setConversionService(createFormattingConversionService())
	            //.setMessageConverters(jacksonMessageConverter)
	            //.setValidator(validator)
	            .build();
	
	}
	
	@Test
	public void testList() throws Exception {
	    ReservationDTO reservation1 = new ReservationDTO();
	    reservation1 = ReservationTest.builReservationDTOTest(1L);
	    
	    ReservationDTO reservation2 = new ReservationDTO();
	    reservation2 = ReservationTest.builReservationDTOTest(2L);
	    
	    //TODO agregar atributos
	    //reservation2.setName("Bob");
	
	    List<ReservationDTO> reservations = Arrays.asList(reservation1, reservation2);
	    
	    Page<ReservationDTO> page = new PageImpl<>(reservations);
	    
	    mockMvc = MockMvcBuilders.standaloneSetup(new ReservationController(reservationQueryService, reservationService))
	    		   .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
	    		   .setViewResolvers(new ViewResolver() {
	    		       @Override
	    		       public View resolveViewName(String viewName, Locale locale) throws Exception {
	    		          return new MappingJackson2JsonView();
	    		      }
	    		   })
	    		   .build();

	    when(reservationQueryService.findByCriteria((ReservationCriteria) any(ReservationCriteria.class), (Pageable) any(Pageable.class))).thenReturn(page);	    
	    
	    mockMvc.perform(get(ReservationController.BASE_URL)
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.data.content", hasSize(2)));
	            
	}

	
    @Test
    public void testGetById() throws Exception {

        //given
        ReservationDTO reservation1 = ReservationTest.builReservationDTOTest(1L);


        when(reservationService.findById(anyLong())).thenReturn(reservation1);

        //when
        mockMvc.perform(get(ReservationController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(ReservationTest.NAME)))
                .andExpect(jsonPath("$.lastName", equalTo(ReservationTest.LAST_NAME)))
                .andExpect(jsonPath("$.email", equalTo(ReservationTest.EMAIL)))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.state", equalTo(ReservationState.CREATED.toString())))
                .andExpect(jsonPath("$.price", equalTo(200)))
                .andExpect(jsonPath("$.dateArrival", equalTo(ReservationTest.DATE_ARRIVAL.toString().replaceAll("T", " "))))
                .andExpect(jsonPath("$.dateDeparture", equalTo(ReservationTest.DATE_DEPARTURE.toString().replaceAll("T", " "))));

    }
   /*
    @Test
    public void createNewReservation() throws Exception {
        //given
        ReservationDTO reservation = ReservationTest.builReservationDTOTest(1L);
        
        ReservationDTO reservationPost = ReservationTest.builReservationDTOTest(1L);

        when(reservationService.createNew( anyLong(), any(ReservationDTO.class))).thenReturn(reservation);

        //when/then
        
        mockMvc.perform(post(ReservationController.BASE_URL)
        	    .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reservationPost)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.name", equalTo(ReservationTest.NAME)))
                .andExpect(jsonPath("$.lastName", equalTo(ReservationTest.LAST_NAME)))
                .andExpect(jsonPath("$.state", equalTo((ReservationState.CREATED.toString()))))
                .andExpect(jsonPath("$.price", equalTo(200)))
                .andExpect(jsonPath("$.dateArrival", equalTo(ReservationTest.DATE_ARRIVAL.toString().replaceAll("T", " "))))
                .andExpect(jsonPath("$.dateDeparture", equalTo(ReservationTest.DATE_DEPARTURE.toString().replaceAll("T", " "))));
                
                
                //.andExpect(jsonPath("$.customer_url", equalTo(ReservationController.BASE_URL + "/1")));
    }
    
	*/
    @Test
    public void testDeleteReservation() throws Exception {

        mockMvc.perform(delete(ReservationController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(reservationService).deleteById(anyLong());
    }
    
    //@Test
    public void testUpdate() throws Exception {
        //given

        ReservationDTO returnDTO = new ReservationDTO();


        when(reservationService.saveByDTO(anyLong(), (ReservationDTO) any(ReservationDTO.class))).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(put(ReservationController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                //TODO agregar atributos
                //.content(asJsonString(reservation)))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.name", equalTo("Fred")));
                //.andExpect(jsonPath("$.customer_url", equalTo(CustomerController.BASE_URL + "/1")));
    }

    //@Test
    public void testPatch() throws Exception {

        //given
    	ReservationDTO reservation = new ReservationDTO();
    	 //TODO agregar atributos
    	//reservation.setName("Fred");

        ReservationDTO returnDTO = new ReservationDTO();
        //TODO agregar atributos
        //returnDTO.setName(reservation.getName());
        //returnDTO.setLastname("Flintstone");
        //returnDTO.setCustomerUrl(CustomerController.BASE_URL + "/1");

        when(reservationService.patch(anyLong(), any(ReservationDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(patch(ReservationController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                //TODO agregar atributos
                //.content(asJsonString(reservation)))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.name", equalTo("Fred")));
                //.andExpect(jsonPath("$.customer_url", equalTo(CustomerController.BASE_URL + "/1")));
    }

    @Test
    public void testNotFoundException() throws Exception {

        when(reservationService.findById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get(ReservationController.BASE_URL + "/222")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
