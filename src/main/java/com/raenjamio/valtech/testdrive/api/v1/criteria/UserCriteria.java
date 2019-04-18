package com.raenjamio.valtech.testdrive.api.v1.criteria;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import lombok.Getter;
import lombok.Setter;

/**
 * Criteria class for the User entity. This class is used in UserResource to
 * receive all the possible filtering options from the Http GET request
 * parameters. For example the following could be a valid requests:
 * <code> /users?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific
 * {@link Filter} class are used, we need to use fix type specific filters.
 */
@Getter
@Setter
public class UserCriteria implements Serializable {

	private static final long serialVersionUID = 4901470095968094015L;

	private LongFilter id;
	
	private StringFilter name;
	
	private StringFilter lastName;
	
	private StringFilter email;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final UserCriteria that = (UserCriteria) o;
		return Objects.equals(id, that.id)  && Objects.equals(name, that.name)
				&& Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, lastName, email);
	}

	@Override
	public String toString() {
		return "UserCriteria{" + (id != null ? "id=" + id + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (lastName != null ? "lastName=" + lastName + ", " : "")
				+ (email != null ? "email=" + email + ", " : "")
				+ "}";
	}

}
