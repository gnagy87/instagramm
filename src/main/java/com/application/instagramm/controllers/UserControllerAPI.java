package com.application.instagramm.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.instagramm.connection.Connection;
import com.application.instagramm.connection.ConnectionService;
import com.application.instagramm.connection.Status;
import com.application.instagramm.dto.AppUserDTO;
import com.application.instagramm.dto.ErrorMessageDTO;
import com.application.instagramm.dto.LoginDTO;
import com.application.instagramm.dto.RegisterDTO;
import com.application.instagramm.dto.StatusMessageDTO;
import com.application.instagramm.exceptions.UserException;
import com.application.instagramm.user.AppUser;
import com.application.instagramm.user.AppUserService;

@RestController
@RequestMapping("/api/user")
public class UserControllerAPI {

	private AppUserService appUserService;

	@Autowired
	public UserControllerAPI(AppUserService appUserService) {
		this.appUserService = appUserService;
	}

	@PostMapping("/registration")
	public ResponseEntity resgistration(@RequestBody RegisterDTO registerDTO) {
		try {
			return ResponseEntity.status(200).body(new StatusMessageDTO("200",
					appUserService.userRegistration(registerDTO).getUsername() + " successfully registered."));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new ErrorMessageDTO("400", e.getMessage()));
		}
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity authenticate(@RequestBody LoginDTO loginDTO) {
		try {
			appUserService.login(loginDTO);
			return ResponseEntity.status(200).body(appUserService.authentication(loginDTO.getUsername()));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new ErrorMessageDTO("400", e.getMessage()));
		}
	}
	
	@GetMapping("/{username}")
	public ResponseEntity getUser(@PathVariable String username) {
		try {
			return ResponseEntity.status(200).body(new AppUserDTO(appUserService.findByUsername(username)));
		} catch (UserException e) {
			return ResponseEntity.status(400).body(new ErrorMessageDTO("400", e.getMessage()));
		}
	}
}
