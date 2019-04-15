package com.raenjamio.valtech.testdrive.api.v1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.raenjamio.valtech.testdrive.api.v1.domain.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	
	Optional<User> findByNameAndLastNameEquals(String name, String lastName);

}
