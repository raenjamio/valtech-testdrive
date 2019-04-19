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

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

/**
 * 
 * @author raenjamio
 * Clase para inicializar la base de datos para desarrollo y test de integracion
 */

@Component
@org.springframework.context.annotation.Profile({"default", "dev", "prod" })
@Slf4j
public class Bootstrap implements CommandLineRunner {

	private final CarRepository carRepository;

	private Car car1 = Car.builder().brand("Ford").description("test").build();
	

	public Bootstrap(CarRepository carRepository) {
		super();
		this.carRepository = carRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Inicializamos la base");
		// List<Profile> profiles = loadProfiles();
		saveCars();
	}
	
	private void saveCars() {
		log.info("gurdamos un auto");
		carRepository.save(car1);
	}
}
