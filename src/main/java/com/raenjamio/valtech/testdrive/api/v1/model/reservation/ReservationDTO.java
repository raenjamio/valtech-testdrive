package com.raenjamio.valtech.testdrive.api.v1.model.reservation;

import java.time.ZonedDateTime;

import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter 
@NoArgsConstructor
public class ReservationDTO {

	private Long id;
	
	//private UserDTO user;
	
	private CarDTO car;
	
	//@JsonFormat(pattern = "YYYY-MM-dd HH:mm")
	//@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	//@JsonDeserialize(using = CustomDateDeserializer.class)
	private ZonedDateTime dateArrival;
	
	//@JsonFormat(pattern = "YYYY-MM-dd HH:mm")
	//@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	//@JsonDeserialize(using = CustomDateDeserializer.class)
	private ZonedDateTime dateDeparture;
	
}
