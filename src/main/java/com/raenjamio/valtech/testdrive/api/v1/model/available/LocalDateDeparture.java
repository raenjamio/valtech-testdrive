package com.raenjamio.valtech.testdrive.api.v1.model.available;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.management.RuntimeErrorException;

import lombok.Data;

@Data
public class LocalDateDeparture {


	private static final int HOUR_DEPARTURE = 18;
	private static final int MINUTE_DEPARTURE = 0;
	
	private LocalDateTime dateDeparture;

	public LocalDateDeparture build(LocalDate dateDeparture) {
		if (dateDeparture == null) {
			throw new RuntimeErrorException(null, " Falta la fecha de partida (dateDeparture)");
		}
		
		this.dateDeparture = dateDeparture.atTime(HOUR_DEPARTURE, MINUTE_DEPARTURE,0);
		
		return this;
	}

	@Override
	public String toString() {
		return "LocalDateDeparture [dateDeparture=" + dateDeparture + "]";
	}
	
	
}
