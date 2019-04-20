package com.raenjamio.valtech.testdrive.api.v1.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.raenjamio.valtech.testdrive.api.v1.domain.Car;
import com.raenjamio.valtech.testdrive.api.v1.mapper.CarMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.CarRepository;
import com.raenjamio.valtech.testdrive.api.v1.service.CarService;
import com.raenjamio.valtech.testdrive.exceptions.NotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CarServiceImpl implements CarService {

	private final CarMapper carMapper;
	private final CarRepository carRepository;

	public CarServiceImpl(CarMapper carMapper, CarRepository carRepository) {
		super();
		this.carMapper = carMapper;
		this.carRepository = carRepository;
	}

	@Override
	public CarDTO findById(Long id) {
		log.debug("@findById id:" + id);
		return carRepository.findById(id).map(x -> carMapper.toDto(x)).map(carDTO -> {
			// set API URL
			// customerDTO.setCustomerUrl(getCustomerUrl(id));
			return carDTO;
		}).orElseThrow(NotFoundException::new);
	}

	@Override
	public CarDTO createNew(CarDTO carDTO) {
		log.debug("@createNew car: " + carDTO);
		return saveAndReturnDTO(carMapper.toEntity(carDTO));
	}

	private CarDTO saveAndReturnDTO(Car car) {
		log.debug("@saveAndReturnDTO car: " + car);
		Car savedCar = carRepository.save(car);

		CarDTO returnDto = carMapper.toDto(savedCar);

		return returnDto;
	}

	@Override
	public CarDTO saveByDTO(Long id, CarDTO carDTO) {
		log.debug("@saveByDTO car: " + carDTO);
		log.debug("@saveByDTO id: " + id);
		Car car = carMapper.toEntity(carDTO);
		car.setId(id);
		return saveAndReturnDTO(car);
	}

	@Override
	public CarDTO patch(Long id, CarDTO carDTO) {
		log.debug("@patch car: " + carDTO);
		log.debug("@patch id: " + id);
		return carRepository.findById(id).map(car -> {
			
			if (carDTO.getBrand() != null) {
				car.setBrand(carDTO.getBrand());
			}
			
			if (carDTO.getDescription() != null) {
				car.setDescription(carDTO.getDescription());
			}

			CarDTO returnDto = carMapper.toDto(carRepository.save(car));

			return returnDto;

		}).orElseThrow(NotFoundException::new);
	}

	@Override
	public Set<CarDTO> findAll() {
		log.debug("@getAll");
		return carRepository.findAll().stream().map(x -> carMapper.toDto(x)).collect(Collectors.toSet());
	}

	@Override
	public CarDTO save(CarDTO object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(CarDTO carDTO) {
		log.debug("@delete {}", carDTO);
		Car car = carMapper.toEntity(carDTO);
		carRepository.delete(car);
	}

	@Override
	public void deleteById(Long id) {
		log.debug("@deleteById {}", id);
		carRepository.deleteById(id);
	}
}
