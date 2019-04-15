package com.raenjamio.valtech.testdrive.api.v1.service;

import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;

public interface ReservationService extends CrudService<ReservationDTO, Long>{


	ReservationDTO createNew(Long idCar, ReservationDTO reservationDTO);
	
	ReservationDTO saveByDTO(Long id, ReservationDTO reservationDTO);

	ReservationDTO patch(Long id, ReservationDTO reservationDTO);

	ReservationDTO createNew(ReservationDTO reservationDTO);

}
