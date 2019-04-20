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

import com.raenjamio.valtech.testdrive.api.usecase.reservation.CreateReservation;
import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation;
import com.raenjamio.valtech.testdrive.api.v1.domain.ReservationState;
import com.raenjamio.valtech.testdrive.api.v1.mapper.ReservationMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.ReservationRepository;
import com.raenjamio.valtech.testdrive.api.v1.service.ReservationService;
import com.raenjamio.valtech.testdrive.api.v1.service.impl.ReservationServiceImpl;
import com.raenjamio.valtech.testdrive.util.ReservationTest;


@ContextConfiguration(classes = ReservationServiceImplTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackageClasses = {ReservationMapper.class})
public class ReservationServiceImplTest {

	@Mock
    ReservationRepository reservationRepository;

	@Autowired
    ReservationMapper reservationMapper;
	
	@Mock
	CreateReservation createReservation;

    ReservationService reservationService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        reservationService = new ReservationServiceImpl(reservationMapper, reservationRepository, createReservation);
    }

    @Test
    public void getAll() throws Exception {
        //given
        Reservation reservation1 = ReservationTest.buildReservationTest(1L);
        
        Reservation reservation2 = ReservationTest.buildReservationTest(2L);
        

        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation1, reservation2));

        //when
        Set<ReservationDTO> reservationDTOS = reservationService.findAll();

        //then
        assertEquals(2, reservationDTOS.size());

    }

    @Test
    public void getById() throws Exception {
        //given
        Reservation reservation1 = ReservationTest.buildReservationTest(1L);
        
        when(reservationRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(reservation1));

        //when
        ReservationDTO reservationDTO = reservationService.findById(1L);

        assertEquals(ReservationTest.NAME, reservationDTO.getName());
        assertEquals(ReservationTest.LAST_NAME, reservationDTO.getLastName());
        assertEquals(ReservationTest.EMAIL, reservationDTO.getEmail());
        assertEquals(new Long(1), reservationDTO.getId());
        assertEquals(ReservationState.CREATED, reservationDTO.getState());
        assertEquals(ReservationTest.DATE_ARRIVAL, reservationDTO.getDateArrival());
        assertEquals(ReservationTest.DATE_DEPARTURE, reservationDTO.getDateDeparture());
    }
    
    @Test
    public void patch() throws Exception {
        //given
        Reservation reservation1 = ReservationTest.buildReservationTest(1L);
        
        ReservationDTO reservationDTO = ReservationTest.builReservationDTOTest(1L);
        reservationDTO.setState(ReservationState.CANCELED);
        
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.ofNullable(reservation1));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation1);
        
        //when
        ReservationDTO savedDto = reservationService.patch(1L, reservationDTO);

        assertEquals(ReservationTest.NAME, savedDto.getName());
        assertEquals(ReservationTest.LAST_NAME, savedDto.getLastName());
        assertEquals(ReservationTest.EMAIL, savedDto.getEmail());
        assertEquals(new Long(1), savedDto.getId());
        assertEquals(ReservationState.CANCELED, savedDto.getState());
        assertEquals(ReservationTest.DATE_ARRIVAL, savedDto.getDateArrival());
        assertEquals(ReservationTest.DATE_DEPARTURE, savedDto.getDateDeparture());
    }

    @Test
    public void createNew() throws Exception {

        //given
        ReservationDTO reservationDTO = ReservationTest.builReservationDTOTest(1L);

        Reservation savedReservation = ReservationTest.buildReservationTest(1L);

        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);
        when(createReservation.create(any(ReservationDTO.class))).thenReturn(reservationDTO);
        //when
        ReservationDTO savedDto = reservationService.createNew(1L, reservationDTO);

        //then
        assertEquals(ReservationTest.NAME, savedDto.getName());
        assertEquals(ReservationTest.LAST_NAME, savedDto.getLastName());
        assertEquals(ReservationTest.EMAIL, savedDto.getEmail());
        assertEquals(new Long(1), savedDto.getId());
        assertEquals(ReservationState.CREATED, savedDto.getState());
        assertEquals(ReservationTest.DATE_ARRIVAL, savedDto.getDateArrival());
        assertEquals(ReservationTest.DATE_DEPARTURE, savedDto.getDateDeparture());
    }

    @Test
    public void saveByDTO() throws Exception {

        //given
        ReservationDTO reservationDTO = ReservationTest.builReservationDTOTest(1L);

        Reservation savedReservation = ReservationTest.buildReservationTest(1L);

        when(reservationRepository.save(any(Reservation.class))).thenReturn(savedReservation);
        when(createReservation.create(any(ReservationDTO.class))).thenReturn(reservationDTO);

        //when
        ReservationDTO savedDto = reservationService.saveByDTO(1L, reservationDTO);


        assertEquals(ReservationTest.NAME, savedDto.getName());
        assertEquals(ReservationTest.LAST_NAME, savedDto.getLastName());
        assertEquals(ReservationTest.EMAIL, savedDto.getEmail());
        assertEquals(new Long(1), savedDto.getId());
        assertEquals(ReservationState.CREATED, savedDto.getState());
        assertEquals(ReservationTest.DATE_ARRIVAL, savedDto.getDateArrival());
        assertEquals(ReservationTest.DATE_DEPARTURE, savedDto.getDateDeparture());
    }

    @Test
    public void deleteReservationById() throws Exception {

        Long id = 1L;

        reservationRepository.deleteById(id);

        verify(reservationRepository, times(1)).deleteById(anyLong());
    }
    
    @Test
    public void deleteReservation() throws Exception {

        Long id = 1L;
        ReservationDTO reservationDTO = ReservationTest.builReservationDTOTest(1L);
        
        reservationService.delete(reservationDTO);

        verify(reservationRepository, times(1)).delete(any());
    }

}
