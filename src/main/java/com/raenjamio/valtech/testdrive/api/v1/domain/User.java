package com.raenjamio.valtech.testdrive.api.v1.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author raenjamio
 * representacion de los usuarios que hacen los test drives
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
public class User implements Serializable {
	
	private static final long serialVersionUID = -2423644965058210269L;
	
	@Builder
	public User(Long id, String name, String lastName, @Email String email, Set<Reservation> reservations) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.reservations = reservations;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String lastName;
	
	@Email
	private String email;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Reservation> reservations = new HashSet<>();

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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

	
}
