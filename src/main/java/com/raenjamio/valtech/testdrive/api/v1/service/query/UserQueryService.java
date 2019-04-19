package com.raenjamio.valtech.testdrive.api.v1.service.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raenjamio.valtech.testdrive.api.v1.criteria.UserCriteria;
import com.raenjamio.valtech.testdrive.api.v1.domain.User;
import com.raenjamio.valtech.testdrive.api.v1.domain.User_;
import com.raenjamio.valtech.testdrive.api.v1.mapper.UserMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.user.UserDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.UserRepository;

import io.github.jhipster.service.QueryService;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for executing complex queries for User entities in the database.
 * The main input is a {@link UserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserDTO} or a {@link Page} of {@link UserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class UserQueryService extends QueryService<User> {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserQueryService(UserRepository UserRepository, UserMapper UserMapper) {
        this.userRepository = UserRepository;
        this.userMapper = UserMapper;
    }

    /**
     * Return a {@link List} of {@link UserDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserDTO> findByCriteria(UserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<User> specification = createSpecification(criteria);
        return userMapper.toDto(userRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
	public Page<UserDTO> findByCriteria(UserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<User> specification = createSpecification(criteria);
        return userRepository.findAll(specification, page)
            .map(userMapper::toDto);
    }


    /**
     * Function to convert UserCriteria to a {@link Specification}
     */
    private Specification<User> createSpecification(UserCriteria criteria) {
        Specification<User> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), User_.id));
            }
            
            if (criteria.getName() != null) {	
                specification = specification.and(buildStringSpecification(criteria.getName(), User_.name));
            }
            
            if (criteria.getLastName() != null) {	
                specification = specification.and(buildStringSpecification(criteria.getLastName(), User_.lastName));
            }
            if (criteria.getEmail() != null) {	
                specification = specification.and(buildStringSpecification(criteria.getEmail(), User_.email));
            }
            
        }
        return specification;
    }
}
