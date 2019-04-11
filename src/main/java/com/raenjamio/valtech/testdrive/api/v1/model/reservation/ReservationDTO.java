package com.raenjamio.valtech.testdrive.api.v1.model.reservation;

import java.time.LocalDateTime;

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
	
	private Long carId;
	
	//@JsonFormat(pattern = "YYYY-MM-dd HH:mm")
	//@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	//@JsonDeserialize(using = CustomDateDeserializer.class)
	private LocalDateTime dateArrival;
	
	//@JsonFormat(pattern = "YYYY-MM-dd HH:mm")
	//@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	//@JsonDeserialize(using = CustomDateDeserializer.class)
	private LocalDateTime dateDeparture;
	
}
