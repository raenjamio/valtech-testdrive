package com.raenjamio.valtech.testdrive.api.v1.service;

import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;

public interface ReservationService extends CrudService<ReservationDTO, Long>{


	ReservationDTO createNew(ReservationDTO userDTO);
	
	ReservationDTO saveByDTO(Long id, ReservationDTO userDTO);

	ReservationDTO patch(Long id, ReservationDTO userDTO);

}
