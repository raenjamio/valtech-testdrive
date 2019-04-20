package com.raenjamio.valtech.testdrive.api.v1.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author raenjamio
 * representacion de la reserva
 */


@Entity
@Setter
@Getter
@NoArgsConstructor
public class Reservation  {
	
	
	@Builder
	public Reservation(Long id, User user, LocalDateTime dateArrival, LocalDateTime dateDeparture, Car car) {
		super();
		this.id = id;
		this.user = user;
		this.dateArrival = dateArrival;
		this.dateDeparture = dateDeparture;
		this.car = car;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
    @JoinColumn(name="user_id", nullable=false)
	private User user;
	
	@NotNull
	private LocalDateTime dateArrival;
	
	@NotNull
	private LocalDateTime dateDeparture;
	
	@ManyToOne
    @JoinColumn(name="car_id", nullable=false)
	private Car car;
	
	@Enumerated(value = EnumType.STRING)
	private ReservationState state = ReservationState.CREATED;
	
    @Column(name = "price", precision = 5, scale = 2, nullable = true)
    private BigDecimal price;

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
		Reservation other = (Reservation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
    
    
	
	
}
