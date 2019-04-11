package com.raenjamio.valtech.testdrive.api.v1.domain;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
	public Reservation(Long id, User user, ZonedDateTime dateArrival, ZonedDateTime dateDeparture, Car car) {
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

	private User user;
	
	private ZonedDateTime dateArrival;
	private ZonedDateTime dateDeparture;
	
	@ManyToOne
    @JoinColumn(name="car_id", nullable=false)
	private Car car;
	
	@Enumerated(value = EnumType.STRING)
	private ReservationState state = ReservationState.CREATED;
	
	
}
