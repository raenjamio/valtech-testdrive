package com.raenjamio.valtech.testdrive.api.v1.model.available;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.raenjamio.valtech.testdrive.api.controller.v1.CarController;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter 
@NoArgsConstructor
@AllArgsConstructor
public class AvailableDTO {
	
	public AvailableDTO(Long carId, LocalDate dateDeparture, LocalDate dateArrival) {
		this.carUrl = CarController.BASE_URL + "/" + carId;
		this.dateDeparture = new LocalDateDeparture().build(dateDeparture).getDateDeparture();
		this.dateArrival = new LocalDateArrival().build(dateArrival).getDateArrival();
		this.reservationUrl = CarController.BASE_URL + "/" + carId + "/reservations";
	}

	private String carUrl;
	
	private LocalDateTime dateArrival;
	
	private LocalDateTime dateDeparture;
	
	private String reservationUrl;
	
		
}
