package com.raenjamio.valtech.testdrive.api.v1.service.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raenjamio.valtech.testdrive.api.v1.criteria.CarCriteria;
import com.raenjamio.valtech.testdrive.api.v1.domain.Car;
import com.raenjamio.valtech.testdrive.api.v1.domain.Car_;
import com.raenjamio.valtech.testdrive.api.v1.mapper.CarMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.car.CarDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.CarRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Service for executing complex queries for Car entities in the database.
 * The main input is a {@link CarCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CarDTO} or a {@link Page} of {@link CarDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class CarQueryService extends QueryService<Car> {

    private final CarRepository carRepository;

    private final CarMapper carMapper;

    public CarQueryService(CarRepository CarRepository, CarMapper CarMapper) {
        this.carRepository = CarRepository;
        this.carMapper = CarMapper;
    }

    /**
     * Return a {@link List} of {@link CarDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CarDTO> findByCriteria(CarCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Car> specification = createSpecification(criteria);
        return carMapper.toDto(carRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CarDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
	public Page<CarDTO> findByCriteria(CarCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.findAll(specification, page)
            .map(carMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CarCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Car> specification = createSpecification(criteria);
        return carRepository.count(specification);
    }

    /**
     * Function to convert CarCriteria to a {@link Specification}
     */
    private Specification<Car> createSpecification(CarCriteria criteria) {
        Specification<Car> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Car_.id));
            }
            /*
            if (criteria.getCreatedById() != null) {	
                specification = specification.and(buildSpecification(criteria.getCreatedById(),
                    root -> root.join(Car_.createdBy, JoinType.LEFT).get(Profile_.id)));
            }
            if (criteria.getTypeId()!= null) {
                specification = specification.and(buildSpecification(criteria.getTypeId(),
                    root -> root.join(Car_.type, JoinType.LEFT).get(CarType_.id)));
            }
            
            if (criteria.getStartDate() != null) {
            	specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Car_.startDate));
            }
            */
        }
        return specification;
    }
}
