/*
 * 
 */
package com.raenjamio.valtech.testdrive.api.v1.service.impl;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.raenjamio.valtech.testdrive.api.usecase.reservation.CreateReservation;
import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation;
import com.raenjamio.valtech.testdrive.api.v1.mapper.ReservationMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.ReservationRepository;
import com.raenjamio.valtech.testdrive.api.v1.service.ReservationService;
import com.raenjamio.valtech.testdrive.exceptions.NotFoundException;
import com.raenjamio.valtech.testdrive.util.Messages;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class ReservationServiceImpl.
 *
 * @author raenjamio
 */

/** The Constant log. */
@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {

	private final ReservationMapper reservationMapper;

	private final ReservationRepository reservationRepository;

	private final CreateReservation createReservation;

	private final Messages messages;

	/**
	 * Instantiates a new reservation service impl.
	 *
	 * @param reservationMapper     the reservation mapper
	 * @param reservationRepository the reservation repository
	 * @param createReservation     the create reservation
	 * @param messages              the messages
	 */
	public ReservationServiceImpl(ReservationMapper reservationMapper, ReservationRepository reservationRepository,
			CreateReservation createReservation, Messages messages) {
		super();
		this.reservationMapper = reservationMapper;
		this.reservationRepository = reservationRepository;
		this.createReservation = createReservation;
		this.messages = messages;
	}

	/**
	 * @param id
	 * @return
	 */
	@Override
	public ReservationDTO findById(Long id) {
		log.debug("@findById id:" + id);
		return reservationRepository.findById(id).map(x -> reservationMapper.toDto(x)).map(reservationDTO -> {
			return reservationDTO;
		}).orElseThrow(NotFoundException::new);
	}

	/**
	 * Servicio para crear una reserva, no puede haber una reserva creada con la misma fecha
	 * @param idCar
	 * @param reservationDTO
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	
	@Transactional
	public synchronized ReservationDTO createNew(Long idCar, ReservationDTO reservationDTO) {
		log.debug("@createNew reservation: " + reservationDTO);
		reservationDTO.setCarId(idCar);
		
		return saveAndReturnDTO(reservationMapper.toEntity(createReservation.create(reservationDTO)));
	}

	/**
	 * @param reservationDTO
	 * @return
	 */
	//@Transactional
	public synchronized ReservationDTO createNew(ReservationDTO reservationDTO) {
		log.debug("@createNew reservation: " + reservationDTO);

		return saveAndReturnDTO(reservationMapper.toEntity(createReservation.create(reservationDTO)));
	}

	/**
	 * Save and return DTO.
	 *
	 * @param reservation the reservation
	 * @return the reservation DTO
	 */
	private ReservationDTO saveAndReturnDTO(Reservation reservation) {
		log.debug("@saveAndReturnDTO reservation: " + reservation);

		Reservation savedReservation = reservationRepository.save(reservation);

		ReservationDTO returnDto = reservationMapper.toDto(savedReservation);

		return returnDto;
	}

	/**
	 * @param id
	 * @param reservationDTO
	 * @return
	 */
	@Override
	public synchronized ReservationDTO saveByDTO(Long id, ReservationDTO reservationDTO) {
		log.debug("@saveByDTO reservation: " + reservationDTO);
		log.debug("@saveByDTO id: " + id);

		Reservation reservation = reservationMapper.toEntity(createReservation.create(reservationDTO));
		reservation.setId(id);
		return saveAndReturnDTO(reservation);
	}

	/**
	 * @param id
	 * @param reservationDTO
	 * @return
	 */
	@Override
	public synchronized ReservationDTO patch(Long id, ReservationDTO reservationDTO) {
		log.debug("@patch reservation: " + reservationDTO);
		log.debug("@patch id: " + id);
		return reservationRepository.findById(id).map(reservation -> {
			if (reservationDTO.getState() != null) {
				reservation.setState(reservationDTO.getState());
			}

			ReservationDTO returnDto = reservationMapper.toDto(reservationRepository.save(reservation));

			return returnDto;

		}).orElseThrow(() -> new NotFoundException(messages.get("reservation.notexist", id.toString())));
	}

	/**
	 * @return
	 */
	@Override
	public Set<ReservationDTO> findAll() {
		log.debug("@getAll");
		return reservationRepository.findAll().stream().map(x -> reservationMapper.toDto(x))
				.collect(Collectors.toSet());
	}

	/**
	 * @param object
	 * @return
	 */
	@Override
	public ReservationDTO save(ReservationDTO object) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param reservationDTO
	 */
	@Override
	public void delete(ReservationDTO reservationDTO) {
		log.debug("@delete {}", reservationDTO);
		Reservation reservation = reservationMapper.toEntity(reservationDTO);
		reservationRepository.delete(reservation);
	}

	/**
	 * @param id
	 */
	@Override
	public void deleteById(Long id) {
		log.debug("@deleteById {}", id);
		reservationRepository.deleteById(id);
	}

}
