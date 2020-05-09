package com.application.instagramm.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.application.instagramm.dto.AuthenticationResponseDTO;
import com.application.instagramm.dto.ConnectionDTO;
import com.application.instagramm.dto.ConnectionFilterDTO;
import com.application.instagramm.dto.LoginDTO;
import com.application.instagramm.dto.RegisterDTO;
import com.application.instagramm.exceptions.LoginException;
import com.application.instagramm.exceptions.RegistrationException;
import com.application.instagramm.exceptions.UserException;
import com.application.instagramm.exceptions.ValidationException;

public interface AppUserService {

	AppUser findByUsername(String username) throws UserException;
	
	AppUser findById(Long userId) throws UserException;

	void login(LoginDTO loginDTO) throws LoginException, UserException;

	List<ConnectionDTO> connectionList(ConnectionFilterDTO connectionRequestDTO) throws UserException;

	AppUser userRegistration(RegisterDTO registerDTO) throws RegistrationException, ValidationException;

	void recordValidity(RegisterDTO registerDTO) throws ValidationException;

	void nameValidity(String name) throws ValidationException;

	void usernameValidity(String username) throws ValidationException;

	void emailValidity(String email) throws ValidationException;

	void passwordValidity(String password) throws ValidationException;

	boolean isExisted(String username);

	void updateAppUser(AppUser appUser) throws UserException;
	
	AuthenticationResponseDTO authentication(String username);
	
	Long getUserIdFromToken(String token);
	
	String getToken(HttpServletRequest request);

}
