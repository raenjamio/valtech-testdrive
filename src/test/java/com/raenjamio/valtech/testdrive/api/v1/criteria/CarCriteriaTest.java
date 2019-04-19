package com.raenjamio.valtech.testdrive.api.v1.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.raenjamio.valtech.testdrive.util.TestUtil;

import io.github.jhipster.service.filter.LongFilter;

public class CarCriteriaTest {
	
	private LongFilter id = new LongFilter();
	private LongFilter id2 = new LongFilter();
	
	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(CarCriteria.class);
		CarCriteria criteria = new CarCriteria();
		criteria.setId(id);
		id.setEquals(1L);
		CarCriteria criteria2 = new CarCriteria();
		criteria2.setId(id2);
		id2.equals(2L);
		
		assertThat(criteria).isNotEqualTo(criteria2);
	}

}
