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

import com.raenjamio.valtech.testdrive.api.v1.domain.Car;
import com.raenjamio.valtech.testdrive.api.v1.mapper.CarMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.CarRepository;
import com.raenjamio.valtech.testdrive.api.v1.service.CarService;
import com.raenjamio.valtech.testdrive.api.v1.service.impl.CarServiceImpl;
import com.raenjamio.valtech.testdrive.util.CarTest;


@ContextConfiguration(classes = CarServiceImplTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackageClasses = {CarMapper.class})
public class CarServiceImplTest {

	private static final String BRAND_MODIF = "brandModif";

	private static final String DESCRIPTION_MODIF = "descriptionModif";

	@Mock
    CarRepository carRepository;

	@Autowired
    CarMapper carMapper;
	
    CarService carService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        carService = new CarServiceImpl(carMapper, carRepository);
    }

    @Test
    public void getAll() throws Exception {
        //given
        Car car1 = CarTest.buildCarTest(1L);
        
        Car car2 = CarTest.buildCarTest(2L);
        

        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2));

        //when
        Set<CarDTO> carDTOS = carService.findAll();

        //then
        assertEquals(2, carDTOS.size());

    }

    @Test
    public void getById() throws Exception {
        //given
        Car car1 = CarTest.buildCarTest(1L);
        
        when(carRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(car1));

        //when
        CarDTO carDTO = carService.findById(1L);

        assertEquals(CarTest.BRAND, carDTO.getBrand());
        assertEquals(new Long(CarTest.ID), carDTO.getId());
        assertEquals(CarTest.DESCRIPTION, carDTO.getDescription());
    }
    
    @Test
    public void patch() throws Exception {
        //given
        Car car1 = CarTest.buildCarTest(1L);
        
        CarDTO carDTO = CarTest.builCarDTOTest(1L);
        carDTO.setDescription(DESCRIPTION_MODIF);
        carDTO.setBrand(BRAND_MODIF);
        
        when(carRepository.findById(anyLong())).thenReturn(Optional.ofNullable(car1));
        when(carRepository.save(any(Car.class))).thenReturn(car1);
        
        //when
        CarDTO savedDto = carService.patch(1L, carDTO);

        assertEquals(savedDto.getBrand(), BRAND_MODIF);
        assertEquals(savedDto.getDescription(), DESCRIPTION_MODIF);
        assertEquals(new Long(1), savedDto.getId());
    }

    @Test
    public void createNew() throws Exception {

        //given
        CarDTO carDTO = CarTest.builCarDTOTest(1L);

        Car savedCar = CarTest.buildCarTest(1L);

        when(carRepository.save(any(Car.class))).thenReturn(savedCar);
        //when
        CarDTO savedDto = carService.createNew(carDTO);

        //then
        assertEquals(CarTest.BRAND, savedDto.getBrand());
        assertEquals(CarTest.DESCRIPTION, savedDto.getDescription());
        assertEquals(new Long(1), savedDto.getId());
    }

    @Test
    public void saveByDTO() throws Exception {

        //given
        CarDTO carDTO = CarTest.builCarDTOTest(1L);

        Car savedCar = CarTest.buildCarTest(1L);

        when(carRepository.save(any(Car.class))).thenReturn(savedCar);

        //when
        CarDTO savedDto = carService.saveByDTO(1L, carDTO);


        assertEquals(CarTest.BRAND, savedDto.getBrand());
        assertEquals(CarTest.DESCRIPTION, savedDto.getDescription());
        assertEquals(new Long(1), savedDto.getId());
    }

    @Test
    public void deleteCarById() throws Exception {

        Long id = 1L;

        carRepository.deleteById(id);

        verify(carRepository, times(1)).deleteById(anyLong());
    }
    
    @Test
    public void deleteCar() throws Exception {

        Long id = 1L;
        CarDTO carDTO = CarTest.builCarDTOTest(1L);
        
        carService.delete(carDTO);

        verify(carRepository, times(1)).delete(any());
    }

}
