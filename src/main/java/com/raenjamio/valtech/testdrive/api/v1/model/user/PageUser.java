package com.raenjamio.valtech.testdrive.api.v1.model.user;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageUser {
	Page<UserDTO> data;
}
