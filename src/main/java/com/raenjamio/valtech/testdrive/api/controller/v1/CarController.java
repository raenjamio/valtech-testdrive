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

/**
 * Punto de entrada de la aplicacion para guardar autos
 * @author raenjamio
 *
 */
@Slf4j
@RestController
@RequestMapping(CarController.BASE_URL)
public class CarController {
	
	public static final String BASE_URL = "/api/v1/cars";
	
	private final CarQueryService carQueryService;
	
	private final CarService carService;
	
	
	
	/**
	 * Instantiates a new car controller.
	 *
	 * @param carQueryService the car query service
	 * @param carService the car service
	 */
	public CarController(CarQueryService carQueryService, CarService carService) {
		super();
		this.carQueryService = carQueryService;
		this.carService = carService;
	}
	
	/**
	 * Gets the all.
	 *
	 * @param criteria the criteria
	 * @param pageable the pageable
	 * @return the all
	 */
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public PageCar getAll(CarCriteria criteria, Pageable pageable) {
		log.debug("REST request to get cars by criteria: {}", criteria);
		Page<CarDTO> page = carQueryService.findByCriteria(criteria, pageable);
		return new PageCar(page);
	}
	
	/**
	 * Delete.
	 *
	 * @param id the id
	 */
	@DeleteMapping({ "/{id}" })
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		log.debug("REST request to delete car : {}", id);
		carService.deleteById(id);
	}

	/**
	 * Update.
	 *
	 * @param id the id
	 * @param carDTO the car DTO
	 * @return the car DTO
	 */
	@PutMapping({ "/{id}" })
	@ResponseStatus(HttpStatus.OK)
	public CarDTO update(@PathVariable Long id, @RequestBody CarDTO carDTO) {
		log.debug("REST request to update car id: {} car {}", id, carDTO);
		return carService.saveByDTO(id, carDTO);
	}

	/**
	 * Patch.
	 *
	 * @param id the id
	 * @param carDTO the car DTO
	 * @return the car DTO
	 */
	@PatchMapping({ "/{id}" })
	@ResponseStatus(HttpStatus.OK)
	public CarDTO patch(@PathVariable Long id, @RequestBody CarDTO carDTO) {
		log.debug("REST request to patch car id: {} car {}", id, carDTO);
		return carService.patch(id, carDTO);
	}
	
	/**
	 * Creates the new.
	 *
	 * @param carDTO the car DTO
	 * @return the car DTO
	 */
	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createNew(@RequestBody CarDTO carDTO){
		log.debug("@createNew car: " + carDTO);
        return carService.createNew(carDTO);
    }
	
	/**
	 * Gets the car.
	 *
	 * @param id the id
	 * @return the car
	 */
	@GetMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public CarDTO getCar(@PathVariable Long id){
		log.debug("@getProfile id: " + id);
        return carService.findById(id);
    }
	

}
