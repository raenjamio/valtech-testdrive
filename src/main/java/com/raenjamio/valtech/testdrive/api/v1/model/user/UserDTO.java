package com.raenjamio.valtech.testdrive.api.v1.model.user;

import java.util.HashSet;
import java.util.Set;

import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter 
@NoArgsConstructor
public class UserDTO {

	private Long id;
	
	private String name;
	private String lastName;
	private String email;
	
	private Set<ReservationDTO> reservations;
	
	
	public UserDTO(String name, String lastName, String email) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.email = email;
	}
	

	public void addReservation(ReservationDTO reservationDTO, Long idUser) {
		reservations.add(reservationDTO);
		reservationDTO.setUserId(idUser);
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
		UserDTO other = (UserDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	



	
}
