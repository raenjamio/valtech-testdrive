package com.raenjamio.valtech.testdrive.api.v1.service.query;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raenjamio.valtech.testdrive.api.v1.criteria.ReservationCriteria;
import com.raenjamio.valtech.testdrive.api.v1.domain.Car_;
import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation;
import com.raenjamio.valtech.testdrive.api.v1.domain.Reservation_;
import com.raenjamio.valtech.testdrive.api.v1.domain.User_;
import com.raenjamio.valtech.testdrive.api.v1.mapper.ReservationMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.reservation.ReservationDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.ReservationRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Service for executing complex queries for Reservation entities in the database.
 * The main input is a {@link ReservationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReservationDTO} or a {@link Page} of {@link ReservationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class ReservationQueryService extends QueryService<Reservation> {

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    public ReservationQueryService(ReservationRepository ReservationRepository, ReservationMapper ReservationMapper) {
        this.reservationRepository = ReservationRepository;
        this.reservationMapper = ReservationMapper;
    }

    /**
     * Return a {@link List} of {@link ReservationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReservationDTO> findByCriteria(ReservationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reservation> specification = createSpecification(criteria);
        return reservationMapper.toDto(reservationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReservationDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
	public Page<ReservationDTO> findByCriteria(ReservationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reservation> specification = createSpecification(criteria);
        return reservationRepository.findAll(specification, page)
            .map(reservationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReservationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Reservation> specification = createSpecification(criteria);
        return reservationRepository.count(specification);
    }

    /**
     * Function to convert ReservationCriteria to a {@link Specification}
     */
    private Specification<Reservation> createSpecification(ReservationCriteria criteria) {
        Specification<Reservation> specification = Specification.where(null);
        if (criteria != null) {
        	
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Reservation_.id));
            }
            
            if (criteria.getCarId() != null) {	
                specification = specification.and(buildSpecification(criteria.getCarId(),
                    root -> root.join(Reservation_.car, JoinType.LEFT).get(Car_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Reservation_.user, JoinType.LEFT).get(User_.id)));
            }
            
            if (criteria.getDateArrival() != null) {
            	specification = specification.and(buildRangeSpecification(criteria.getDateArrival(), Reservation_.dateArrival));
            }
            if (criteria.getDateDeparture() != null) {
            	specification = specification.and(buildRangeSpecification(criteria.getDateDeparture(), Reservation_.dateDeparture));
            }
            
        }
        return specification;
    }
}
