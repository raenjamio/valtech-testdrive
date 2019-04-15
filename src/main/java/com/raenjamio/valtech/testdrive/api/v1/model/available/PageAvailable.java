package com.raenjamio.valtech.testdrive.api.v1.model.available;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageAvailable {
	List<AvailableDTO> data;
}
