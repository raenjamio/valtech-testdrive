package com.raenjamio.valtech.testdrive.api.v1.model.car;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter 
@NoArgsConstructor
public class CarDTO {

	private Long id;
	
	private String description;
	
	private String brand;
	
}
