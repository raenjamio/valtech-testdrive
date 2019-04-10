package com.raenjamio.valtech.testdrive.api.v1.model.car;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageCar {
	Page<CarDTO> data;
}
