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
		Car other = (Car) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
