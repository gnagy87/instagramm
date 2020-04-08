package com.application.instagramm.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.application.instagramm.connection.Connection;
import com.application.instagramm.dto.AppUserDTO;
import com.application.instagramm.dto.ConnectionDTO;
import com.application.instagramm.dto.LoginDTO;
import com.application.instagramm.dto.RegisterDTO;
import com.application.instagramm.exceptions.LoginException;
import com.application.instagramm.exceptions.RegistrationException;
import com.application.instagramm.exceptions.UserException;
import com.application.instagramm.exceptions.ValidationException;
import com.application.instagramm.utils.ValidationService;

@Service
public class AppUserServiceImpl implements AppUserService {

	private AppUserRepository appUserRepository;
	private ValidationService validationService;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public AppUserServiceImpl(AppUserRepository appUserRepository, ValidationService validationService,
			PasswordEncoder passwordEncoder) {
		this.appUserRepository = appUserRepository;
		this.validationService = validationService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public AppUser findByUsername(String username) throws UserException {
		return appUserRepository.findAppUserByUsername(username)
				.orElseThrow(() -> new UserException("User does not exist!"));
	}

	@Override
	public void login(LoginDTO loginDTO) throws LoginException, UserException {
		AppUser appUser = findByUsername(loginDTO.getUsername());
		if (!passwordEncoder.matches(loginDTO.getPassword(), appUser.getPassword())) {
			throw new LoginException("Wrong password!");
		}
	}

	@Override
	public List<ConnectionDTO> connectionList(AppUser appUser) {
		List<ConnectionDTO> result = new ArrayList<>();
		for (Connection connection : appUser.getConnections()) {
			AppUserDTO userDTO = new AppUserDTO();
			userDTO.setId(connection.getFriend().getId());
			userDTO.setFirstname(connection.getFriend().getFirstName());
			userDTO.setLastname(connection.getFriend().getLastName());
			userDTO.setEmail(connection.getFriend().getEmail());
			ConnectionDTO connectionDTO = new ConnectionDTO(userDTO, connection.getStatus(),
					connection.getResponseDate());
			result.add(connectionDTO);
		}
		return result;
	}

	@Override
	public AppUser userRegistration(RegisterDTO registerDTO) throws RegistrationException, ValidationException {
		recordValidity(registerDTO);
		if (isExisted(registerDTO.getUsername())) {
			throw new RegistrationException("Username already taken!");
		}
		return saveAppUser(registerDTO);
	}

	@Override
	public void recordValidity(RegisterDTO registerDTO) throws ValidationException {
		nameValidity(registerDTO.getFirstName());
		nameValidity(registerDTO.getLastName());
		usernameValidity(registerDTO.getUsername());
		emailValidity(registerDTO.getEmail());
		passwordValidity(registerDTO.getPassword());
	}

	@Override
	public void nameValidity(String name) throws ValidationException {
		if (!validationService.nameValidator(name)) {
			throw new ValidationException(
					"Name is not valid! Length must be in 2-20 characters and only letters are allowed!");
		}
	}

	@Override
	public void usernameValidity(String username) throws ValidationException {
		if (!validationService.usernameValidator(username)) {
			throw new ValidationException(
					"Username is not valid! Length must be in 4-16 characters and [-a-zA-Z0-9!?._+] characters are allowed!");
		}
	}

	@Override
	public void emailValidity(String email) throws ValidationException {
		if (!validationService.emailValidator(email)) {
			throw new ValidationException("Email format is not valid!");
		}
	}

	@Override
	public void passwordValidity(String password) throws ValidationException {
		if (!validationService.passwordValidator(password)) {
			throw new ValidationException(
					"Password is not valid! Length must be in 6-16 characters and [-a-zA-Z0-9!?._+] characters are allowed!");
		}
	}

	@Override
	public boolean isExisted(String username) {
		return appUserRepository.findAppUserByUsername(username).isPresent();
	}

	@Override
	public AppUser saveAppUser(RegisterDTO registerDTO) {
		AppUser user = new AppUser(registerDTO.getUsername(), passwordEncoder.encode(registerDTO.getPassword()),
				registerDTO.getEmail(), registerDTO.getFirstName(), registerDTO.getLastName());
		appUserRepository.save(user);
		return user;
	}

}
