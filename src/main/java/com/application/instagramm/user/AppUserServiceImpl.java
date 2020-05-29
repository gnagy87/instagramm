package com.application.instagramm.user;

import java.util.List;
import java.util.stream.Collectors;

import com.application.instagramm.mappers.AppUserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.application.instagramm.dto.AppUserDTO;
import com.application.instagramm.dto.AuthenticationResponseDTO;
import com.application.instagramm.dto.ConnectionDTO;
import com.application.instagramm.dto.LoginDTO;
import com.application.instagramm.dto.RegisterDTO;
import com.application.instagramm.exceptions.LoginException;
import com.application.instagramm.exceptions.RegistrationException;
import com.application.instagramm.exceptions.UserException;
import com.application.instagramm.exceptions.ValidationException;
import com.application.instagramm.security.JwtUtil;
import com.application.instagramm.security.MyUserDetails;
import com.application.instagramm.security.MyUserDetailsService;
import com.application.instagramm.utils.ValidationService;

@Service
public class AppUserServiceImpl implements AppUserService {

	private final AppUserMapper appUserMapper;
	private AppUserRepository appUserRepository;
	private ValidationService validationService;
	private PasswordEncoder passwordEncoder;
	private MyUserDetailsService userDetailsService;
	private JwtUtil jwtTokenUtil;

	public AppUserServiceImpl(AppUserMapper appUserMapper, AppUserRepository appUserRepository, ValidationService validationService,
							  PasswordEncoder passwordEncoder, MyUserDetailsService userDetailsService, JwtUtil jwtTokenUtil) {
		this.appUserMapper = appUserMapper;
		this.appUserRepository = appUserRepository;
		this.validationService = validationService;
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
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
	public List<ConnectionDTO> getConnectionList(AppUser appUser) {
		return appUser.getConnections().stream()
				.map(c -> {
					AppUserDTO friendDTO = appUserMapper.appUserToAppUserDTO(c.getFriend());
					ConnectionDTO connectionDTO = new ConnectionDTO(friendDTO, c.getStatus(),
							c.getResponseDate());
					return connectionDTO;
				})
				.collect(Collectors.toList());
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

	@Override
	public AuthenticationResponseDTO authentication(String username) {
		final MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserByUsername(username);
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		return new AuthenticationResponseDTO(jwt);
	}

	@Override
	public Long getUserIdFromToken(String token) {
		return Long.parseLong(jwtTokenUtil.extractUserId(token));
	}
}
