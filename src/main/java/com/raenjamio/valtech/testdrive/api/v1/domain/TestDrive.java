package com.raenjamio.valtech.testdrive.api.v1.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * 
 * @author raenjamio
 * representacion de los test drives
 */

@Entity
@Data
public class TestDrive implements Serializable  {

	private static final long serialVersionUID = -3876305526935903278L;
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long id;
	
	private Double cost;
	
	
}
