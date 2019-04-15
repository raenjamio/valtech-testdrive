package com.raenjamio.valtech.testdrive.api.v1.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.raenjamio.valtech.testdrive.api.usecase.reservation.CreateReservation;
import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation;
import com.raenjamio.valtech.testdrive.api.v1.mapper.ReservationMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.ReservationRepository;
import com.raenjamio.valtech.testdrive.api.v1.service.ReservationService;
import com.raenjamio.valtech.testdrive.exceptions.NotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {

	private final ReservationMapper reservationMapper;
	private final ReservationRepository reservationRepository;
	private final CreateReservation createReservation;

	public ReservationServiceImpl(ReservationMapper reservationMapper, ReservationRepository reservationRepository,
			CreateReservation createReservation) {
		super();
		this.reservationMapper = reservationMapper;
		this.reservationRepository = reservationRepository;
		this.createReservation = createReservation;
	}

	@Override
	public ReservationDTO findById(Long id) {
		log.debug("@findById id:" + id);
		return reservationRepository.findById(id).map(x -> reservationMapper.toDto(x)).map(reservationDTO -> {
			return reservationDTO;
		}).orElseThrow(NotFoundException::new);
	}

	@Override
	public ReservationDTO createNew(Long idCar, ReservationDTO reservationDTO) {
		log.debug("@createNew reservation: " + reservationDTO);
		reservationDTO.setCarId(idCar);
		
		return saveAndReturnDTO(reservationMapper.toEntity(createReservation.create(reservationDTO)));
	}
	
	public ReservationDTO createNew(ReservationDTO reservationDTO) {
		log.debug("@createNew reservation: " + reservationDTO);
		
		return saveAndReturnDTO(reservationMapper.toEntity(createReservation.create(reservationDTO)));
	}

	private ReservationDTO saveAndReturnDTO(Reservation reservation) {
		log.debug("@saveAndReturnDTO reservation: " + reservation);
		
		Reservation savedReservation = reservationRepository.save(reservation);

		ReservationDTO returnDto = reservationMapper.toDto(savedReservation);

		return returnDto;
	}

	@Override
	public ReservationDTO saveByDTO(Long id, ReservationDTO reservationDTO) {
		log.debug("@saveByDTO reservation: " + reservationDTO);
		log.debug("@saveByDTO id: " + id);
		
		Reservation reservation = reservationMapper.toEntity(createReservation.create(reservationDTO));
		reservation.setId(id);
		return saveAndReturnDTO(reservation);
	}

	@Override
	public ReservationDTO patch(Long id, ReservationDTO reservationDTO) {
		log.debug("@patch reservation: " + reservationDTO);
		log.debug("@patch id: " + id);
		return reservationRepository.findById(id).map(reservation -> {
			if (reservationDTO.getState() != null) {
				reservation.setState(reservationDTO.getState());
			}

			ReservationDTO returnDto = reservationMapper.toDto(reservationRepository.save(reservation));

			return returnDto;

		}).orElseThrow(NotFoundException::new);
	}

	@Override
	public Set<ReservationDTO> findAll() {
		log.debug("@getAll");
		return reservationRepository.findAll().stream().map(x -> reservationMapper.toDto(x))
				.collect(Collectors.toSet());
	}

	@Override
	public ReservationDTO save(ReservationDTO object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ReservationDTO reservationDTO) {
		log.debug("@delete {}", reservationDTO);
		Reservation reservation = reservationMapper.toEntity(reservationDTO);
		reservationRepository.delete(reservation);
	}

	@Override
	public void deleteById(Long id) {
		log.debug("@deleteById {}", id);
		reservationRepository.deleteById(id);
	}

}
