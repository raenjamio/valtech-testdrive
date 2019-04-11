package com.raenjamio.valtech.testdrive.api.v1.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author raenjamio representacion del auto a testear en este caso es uno solo
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Car {

	@Builder
	public Car(Long id, String description, String brand) {
		super();
		this.id = id;
		this.description = description;
		this.brand = brand;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String description;

	private String brand;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "car")
	private Set<Reservation> reservations = new HashSet<>();
	
	public void addReservation(Reservation reservation) {
		reservations.add(reservation);
		reservation.setCar(this);
	}
}
