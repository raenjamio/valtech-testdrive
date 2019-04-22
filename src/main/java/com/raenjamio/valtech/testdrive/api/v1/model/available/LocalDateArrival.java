package com.raenjamio.valtech.testdrive.api.v1.model.available;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.management.RuntimeErrorException;

import lombok.Data;
/**
 * 
 * @author raenjamio
 * Hacemos un poco de DDD y hacemos el value object para la fecha de llegada
 */
@Data
public class LocalDateArrival {
	

	public static final int DAY_RESERVATION_MAX = 1;
	private static final int HOUR_ARRIVAL = 10;
	private static final int MINUTE_ARRIVAL = 0;
	
	private LocalDateTime dateArrival;
	
	

	public LocalDateArrival build(LocalDate dateDeparture) {
		if (dateDeparture == null) {
			throw new RuntimeErrorException(null, " Falta la fecha de llegada (dateArrival)");
		}
		
		this.dateArrival = dateDeparture.plusDays(DAY_RESERVATION_MAX).atTime(HOUR_ARRIVAL, MINUTE_ARRIVAL, 0);
		
		return this;
	}

	@Override
	public String toString() {
		return "LocalDateArrival [dateArrival=" + dateArrival + "]";
	}
	
	
}
