package com.raenjamio.valtech.testdrive.api.controller.v1;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.raenjamio.valtech.testdrive.api.v1.criteria.CarCriteria;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;
import com.raenjamio.valtech.testdrive.api.v1.model.car.PageCar;
import com.raenjamio.valtech.testdrive.api.v1.service.CarService;
import com.raenjamio.valtech.testdrive.api.v1.service.query.CarQueryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(CarController.BASE_URL)
public class CarController {
	
	public static final String BASE_URL = "/api/v1/cars";
	
	private final CarQueryService carQueryService;
	private final CarService carService;
	
	
	
	public CarController(CarQueryService carQueryService, CarService carService) {
		super();
		this.carQueryService = carQueryService;
		this.carService = carService;
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public PageCar getAll(CarCriteria criteria, Pageable pageable) {
		log.debug("REST request to get cars by criteria: {}", criteria);
		Page<CarDTO> page = carQueryService.findByCriteria(criteria, pageable);
		return new PageCar(page);
	}
	
	@DeleteMapping({ "/{id}" })
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		log.debug("REST request to delete car : {}", id);
		carService.deleteById(id);
	}

	@PutMapping({ "/{id}" })
	@ResponseStatus(HttpStatus.OK)
	public CarDTO update(@PathVariable Long id, @RequestBody CarDTO carDTO) {
		log.debug("REST request to update car id: {} car {}", id, carDTO);
		return carService.saveByDTO(id, carDTO);
	}

	@PatchMapping({ "/{id}" })
	@ResponseStatus(HttpStatus.OK)
	public CarDTO patch(@PathVariable Long id, @RequestBody CarDTO carDTO) {
		log.debug("REST request to patch car id: {} car {}", id, carDTO);
		return carService.patch(id, carDTO);
	}
	
	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createNew(@RequestBody CarDTO carDTO){
		log.debug("@createNew car: " + carDTO);
        return carService.createNew(carDTO);
    }
	
	@GetMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public CarDTO getCar(@PathVariable Long id){
		log.debug("@getProfile id: " + id);
        return carService.findById(id);
    }
	

}
