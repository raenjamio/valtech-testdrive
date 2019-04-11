package com.raenjamio.valtech.testdrive.api.v1.repository;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

	//Set<Reservation>  findAllByReservationDateDepartureBetweenDateArrival(LocalDateTime dateDeparture, LocalDateTime dateArrival);
	
}
