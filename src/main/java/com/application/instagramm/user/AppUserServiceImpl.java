package com.application.instagramm.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.application.instagramm.connection.Connection;
import com.application.instagramm.connection.Status;
import com.application.instagramm.dto.AppUserDTO;
import com.application.instagramm.dto.AuthenticationResponseDTO;
import com.application.instagramm.dto.ConnectionDTO;
import com.application.instagramm.dto.ConnectionFilterDTO;
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

	private AppUserRepository appUserRepository;
	private ValidationService validationService;
	private PasswordEncoder passwordEncoder;
	private MyUserDetailsService userDetailsService;
	private JwtUtil jwtTokenUtil;

	@Autowired
	public AppUserServiceImpl(AppUserRepository appUserRepository, ValidationService validationService,
			PasswordEncoder passwordEncoder, MyUserDetailsService userDetailsService, JwtUtil jwtTokenUtil) {
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
	public AppUser findById(Long userId) throws UserException {
		return appUserRepository.findById(userId)
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
	public List<ConnectionDTO> connectionList(ConnectionFilterDTO connectionRequestDTO) throws UserException {
		AppUser user = findByUsername(connectionRequestDTO.getUsername());
		List<ConnectionDTO> connectionsList = new ArrayList<>();
		user.getConnections().forEach((connection) -> {
			if (connectionRequestDTO.getStatus().equals("accepted") && connection.getStatus().toString().equals("ACCEPTED")) {
				connectionsList.add(connectionDTOProvider(
						friendProviderFromConnection(user.getId(), connection), connection.getStatus(), connection.getRequestSent()));
			} else {
				ConnectionDTO connectionDTO = new ConnectionDTO();
				if (connectionRequestDTO.isInvitedUser()) {
					connectionDTO = connectionDTOProvider(InvitedUserProviderFromConnection(user.getId(), connectionRequestDTO.getStatus(), connection), 
							connection.getStatus(), connection.getRequestSent());
				} else {
					connectionDTO = connectionDTOProvider(AppUserProviderFromConnection(user.getId(), connectionRequestDTO.getStatus(), connection), 
							connection.getStatus(), connection.getRequestSent());
				}
				if (connectionDTO != null) {
					connectionsList.add(connectionDTO);
				}
			}
		});
		return connectionsList;
	}
	
	public AppUser friendProviderFromConnection(Long userId, Connection connection) {
		if (userId == connection.getAppUser().getId()) {
			return connection.getInvitedUser();
		}
		return connection.getAppUser();
	}
	
	public ConnectionDTO connectionDTOProvider(AppUser user, Status status, Timestamp timestamp) {
		return new ConnectionDTO(new AppUserDTO(user.getId(),user.getUsername(), user.getFirstName(),
				user.getLastName(), user.getEmail()), status, timestamp);
	}
	
	public AppUser InvitedUserProviderFromConnection(Long userId, String string, Connection connection) {
		if (userId == connection.getInvitedUser().getId() && connection.getStatus().toString().toLowerCase().equals(string)) {
			return connection.getAppUser();
		}
		return null;
	}
	
	public AppUser AppUserProviderFromConnection(Long userId, String string, Connection connection) {
		if (userId == connection.getAppUser().getId() && connection.getStatus().toString().toLowerCase().equals(string)) {
			return connection.getInvitedUser();
		}
		return null;	
	}

	@Override
	public AppUser userRegistration(RegisterDTO registerDTO) throws RegistrationException, ValidationException {
		recordValidity(registerDTO);
		if (isExisted(registerDTO.getUsername())) {
			throw new RegistrationException("Username already taken!");
		}
		AppUser newUser = new AppUser(registerDTO.getUsername(), passwordEncoder.encode(registerDTO.getPassword()),
				registerDTO.getEmail(), registerDTO.getFirstName(), registerDTO.getLastName());
		appUserRepository.save(newUser);
		return newUser;
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
	public void updateAppUser(AppUser appUser) throws UserException {
		if (isExisted(appUser.getUsername())) {
			appUserRepository.save(appUser);
		} else {
			throw new UserException("User has not been registered!");
		}
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

	@Override
	public String getToken(HttpServletRequest request) {
		return request.getHeader("Authorization").substring(7);
	}
	
}
