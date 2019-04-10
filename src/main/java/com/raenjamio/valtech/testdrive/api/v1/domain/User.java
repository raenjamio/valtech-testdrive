package com.raenjamio.valtech.testdrive.api.v1.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;

import lombok.Data;

/**
 * 
 * @author raenjamio
 * representacion de los usuarios que hacen los test drives
 */

@Data
@Entity
public class User implements Serializable {
	
	private static final long serialVersionUID = -2423644965058210269L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String lastname;
	
	@Email
	private String email;

}
