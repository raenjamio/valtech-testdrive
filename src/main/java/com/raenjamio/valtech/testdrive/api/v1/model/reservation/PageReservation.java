package com.raenjamio.valtech.testdrive.api.v1.model.reservation;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageReservation {
	Page<ReservationDTO> data;
}
