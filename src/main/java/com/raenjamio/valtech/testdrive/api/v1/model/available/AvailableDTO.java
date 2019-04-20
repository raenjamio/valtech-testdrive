package com.raenjamio.valtech.testdrive.api.v1.model.available;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.raenjamio.valtech.testdrive.api.controller.v1.CarController;
import com.raenjamio.valtech.testdrive.util.CustomDateDeserializer;
import com.raenjamio.valtech.testdrive.util.CustomLocalDateTimeSerializer;

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
	
	@JsonFormat(pattern = "YYYY-MM-dd HH:mm")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private LocalDateTime dateArrival;
	
	@JsonFormat(pattern = "YYYY-MM-dd HH:mm")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private LocalDateTime dateDeparture;
	
	private String reservationUrl;
	
		
}
