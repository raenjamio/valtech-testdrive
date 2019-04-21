package com.raenjamio.valtech.testdrive.api.controller.v1;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.raenjamio.valtech.testdrive.api.v1.criteria.UserCriteria;
import com.raenjamio.valtech.testdrive.api.v1.model.user.PageUser;
import com.raenjamio.valtech.testdrive.api.v1.model.user.UserDTO;
import com.raenjamio.valtech.testdrive.api.v1.service.UserService;
import com.raenjamio.valtech.testdrive.api.v1.service.query.UserQueryService;

import lombok.extern.slf4j.Slf4j;

/**
 * Punto de entrada para crear usuario, aunque en el ejercicio no se pidio 
 * se agrego
 * @author raenjamio
 *
 */
@Slf4j
@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {
	
	public static final String BASE_URL = "/api/v1/users";
	
	private final UserQueryService userQueryService;
	private final UserService userService;
	
	
	/**
	 * Instantiates a new user controller.
	 *
	 * @param userQueryService the user query service
	 * @param userService the user service
	 */
	public UserController(UserQueryService userQueryService, UserService userService) {
		super();
		this.userQueryService = userQueryService;
		this.userService = userService;
	}
	
	/**
	 * Gets the all.
	 *
	 * @param criteria the criteria
	 * @param pageable the pageable
	 * @return the all
	 */
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public PageUser getAll(UserCriteria criteria, Pageable pageable) {
		log.debug("REST request to get Users by criteria: {}", criteria);
		Page<UserDTO> page = userQueryService.findByCriteria(criteria, pageable);
		return new PageUser(page);
	}
	
	/**
	 * Delete.
	 *
	 * @param id the id
	 */
	@DeleteMapping({ "/{id}" })
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		log.debug("REST request to delete User : {}", id);
		userService.deleteById(id);
	}

	/**
	 * Update.
	 *
	 * @param id the id
	 * @param UserDTO the user DTO
	 * @return the user DTO
	 */
	@PutMapping({ "/{id}" })
	@ResponseStatus(HttpStatus.OK)
	public UserDTO update(@PathVariable Long id, @RequestBody UserDTO UserDTO) {
		log.debug("REST request to update User id: {} User {}", id, UserDTO);
		return userService.saveByDTO(id, UserDTO);
	}

	/**
	 * Patch.
	 *
	 * @param id the id
	 * @param UserDTO the user DTO
	 * @return the user DTO
	 */
	@PatchMapping({ "/{id}" })
	@ResponseStatus(HttpStatus.OK)
	public UserDTO patch(@PathVariable Long id, @RequestBody UserDTO UserDTO) {
		log.debug("REST request to patch User id: {} User {}", id, UserDTO);
		return userService.patch(id, UserDTO);
	}
	
	/**
	 * Creates the new.
	 *
	 * @param userDTO the user DTO
	 * @return the user DTO
	 */
	@PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createNew(@RequestBody UserDTO userDTO){
		log.debug("@createNew user: " + userDTO);
        return userService.createNew(userDTO);
    }
	
	/**
	 * Gets the user.
	 *
	 * @param id the id
	 * @return the user
	 */
	@GetMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(@PathVariable Long id){
		log.debug("@getUser id: " + id);
        return userService.findById(id);
    } 

}
