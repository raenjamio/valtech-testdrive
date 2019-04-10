package com.raenjamio.valtech.testdrive.boostrap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.raenjamio.valtech.testdrive.api.v1.domain.Car;
import com.raenjamio.valtech.testdrive.api.v1.repository.CarRepository;

import net.bytebuddy.utility.RandomString;

/**
 * 
 * @author raenjamio
 * Clase para inicializar la base de datos para desarrollo y test de integracion
 */

@Component
@org.springframework.context.annotation.Profile({"default", "dev" })
public class Bootstrap implements CommandLineRunner {

	private final CarRepository carRepository;

	private Car car1 = Car.builder().brand("Ford").description("test").build();
	

	public Bootstrap(CarRepository carRepository) {
		super();
		this.carRepository = carRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		// List<Profile> profiles = loadProfiles();
		saveCars();
	}
	
	private void saveCars() {
		carRepository.save(car1);
	}
}
