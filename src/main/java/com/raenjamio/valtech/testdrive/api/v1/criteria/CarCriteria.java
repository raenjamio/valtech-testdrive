package com.raenjamio.valtech.testdrive.api.v1.criteria;

import java.io.Serializable;
import java.util.Objects;

import com.raenjamio.valtech.testdrive.service.filter.LongFilter;

import lombok.Getter;
import lombok.Setter;

/**
 * Criteria class for the Car entity. This class is used in CarResource to
 * receive all the possible filtering options from the Http GET request
 * parameters. For example the following could be a valid requests:
 * <code> /cars?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific
 * {@link Filter} class are used, we need to use fix type specific filters.
 */
@Getter
@Setter
public class CarCriteria implements Serializable {

	private static final long serialVersionUID = 4901470095968094015L;

	private LongFilter id;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final CarCriteria that = (CarCriteria) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "CarCriteria{" + (id != null ? "id=" + id + ", " : "")
				+ "}";
	}

}
