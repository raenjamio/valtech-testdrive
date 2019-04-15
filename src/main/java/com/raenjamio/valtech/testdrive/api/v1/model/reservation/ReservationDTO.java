package com.raenjamio.valtech.testdrive.api.v1.model.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.raenjamio.valtech.testdrive.api.v1.domain.ReservationState;
import com.raenjamio.valtech.testdrive.util.CustomDateDeserializer;
import com.raenjamio.valtech.testdrive.util.CustomLocalDateTimeSerializer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReservationDTO {

	private Long id;

	private Long userId;

	private Long carId;
	
	@JsonFormat(pattern = "YYYY-MM-dd HH:mm")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private LocalDateTime dateArrival;

	@JsonFormat(pattern = "YYYY-MM-dd HH:mm")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private LocalDateTime dateDeparture;

	private ReservationState state = ReservationState.CREATED;
	
    private BigDecimal price;
    
    private String name;
    
    private String lastName;
    
    private String email;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReservationDTO other = (ReservationDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
    
    

}
