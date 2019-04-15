package com.raenjamio.valtech.testdrive.api.v1.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.raenjamio.valtech.testdrive.api.v1.domain.User;
import com.raenjamio.valtech.testdrive.api.v1.mapper.UserMapper;
import com.raenjamio.valtech.testdrive.api.v1.model.user.UserDTO;
import com.raenjamio.valtech.testdrive.api.v1.repository.UserRepository;
import com.raenjamio.valtech.testdrive.api.v1.service.UserService;
import com.raenjamio.valtech.testdrive.exceptions.NotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final UserRepository userRepository;

	public UserServiceImpl(UserMapper userMapper, UserRepository userRepository) {
		super();
		this.userMapper = userMapper;
		this.userRepository = userRepository;
	}

	@Override
	public UserDTO findById(Long id) {
		log.debug("@findById id:" + id);
		return userRepository.findById(id).map(x -> userMapper.toDto(x)).map(userDTO -> {
			// set API URL
			// customerDTO.setCustomerUrl(getCustomerUrl(id));
			return userDTO;
		}).orElseThrow(NotFoundException::new);
	}

	@Override
	public UserDTO createNew(UserDTO userDTO) {
		log.debug("@createNew user: " + userDTO);
		return saveAndReturnDTO(userMapper.toEntity(userDTO));
	}

	private UserDTO saveAndReturnDTO(User user) {
		log.debug("@saveAndReturnDTO user: " + user);
		User savedUser = userRepository.save(user);

		UserDTO returnDto = userMapper.toDto(savedUser);

		return returnDto;
	}

	@Override
	public UserDTO saveByDTO(Long id, UserDTO userDTO) {
		log.debug("@saveByDTO user: " + userDTO);
		log.debug("@saveByDTO id: " + id);
		User user = userMapper.toEntity(userDTO);
		user.setId(id);
		return saveAndReturnDTO(user);
	}

	@Override
	public UserDTO patch(Long id, UserDTO userDTO) {
		log.debug("@patch user: " + userDTO);
		log.debug("@patch id: " + id);
		return userRepository.findById(id).map(user -> {

			UserDTO returnDto = userMapper.toDto(userRepository.save(user));

			return returnDto;

		}).orElseThrow(NotFoundException::new);
	}

	@Override
	public Set<UserDTO> findAll() {
		log.debug("@getAll");
		return userRepository.findAll().stream().map(x -> userMapper.toDto(x)).collect(Collectors.toSet());
	}

	@Override
	public UserDTO save(UserDTO object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(UserDTO userDTO) {
		log.debug("@delete {}", userDTO);
		User user = userMapper.toEntity(userDTO);
		userRepository.delete(user);
	}

	@Override
	public void deleteById(Long id) {
		log.debug("@deleteById {}", id);
		userRepository.deleteById(id);
	}

	@Override
	public Optional<UserDTO> findByNameAndLastNameEquals(String name, String lastName) {
		log.debug("@findByNameAndLastNameEquals {} {}", name, lastName);
		Optional<User> user = userRepository.findByNameAndLastNameEquals(name, lastName);
		if (user.isPresent()) {
			return Optional.of(userMapper.toDto(user.get()));
		}
		return Optional.empty();
	}
}
