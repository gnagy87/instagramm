package com.application.instagramm.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.instagramm.connection.Connection;
import com.application.instagramm.connection.ConnectionService;
import com.application.instagramm.connection.Status;
import com.application.instagramm.dto.ErrorMessageDTO;
import com.application.instagramm.dto.LoginDTO;
import com.application.instagramm.dto.RegisterDTO;
import com.application.instagramm.dto.StatusMessageDTO;
import com.application.instagramm.user.AppUser;
import com.application.instagramm.user.AppUserService;

@RestController
@RequestMapping("/api/user")
public class UserControllerAPI {

	private AppUserService appUserService;
	private ConnectionService connectionService;

	@Autowired
	public UserControllerAPI(AppUserService appUserService, ConnectionService connectionService) {
		this.appUserService = appUserService;
		this.connectionService = connectionService;
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

	@GetMapping("/login")
	public ResponseEntity login(@RequestBody LoginDTO loginDTO) {
		try {
			appUserService.login(loginDTO);
			return ResponseEntity.status(200).body("OK");
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new ErrorMessageDTO("400", e.getMessage()));
		}
	}

	@PostMapping("/addfriend")
	public ResponseEntity addFriend() {
		AppUser user = new AppUser("username", "password", "email", "first", "last");
		AppUser user2 = new AppUser("username2", "password2", "email2", "first2", "last2");
		AppUser user3 = new AppUser("username3", "password3", "email3", "first3", "last3");
		AppUser user4 = new AppUser("username4", "password4", "email4", "first4", "last4");

		// appUserService.saveAppUser(user);
		// appUserService.saveAppUser(user2);
		// appUserService.saveAppUser(user3);
		// appUserService.saveAppUser(user4);

		Connection af = new Connection();
		af.setStatus(Status.PENDING);
		af.setAppUser(user);
		af.setFriend(user2);
		connectionService.saveConnection(af);
		Set<Connection> connections = user.getConnections();
		connections.add(af);
		user.setConnections(connections);
		// appUserService.saveAppUser(user);

		Connection af2 = new Connection();
		af2.setStatus(Status.PENDING);
		af2.setAppUser(user4);
		af2.setFriend(user);
		connectionService.saveConnection(af2);
		// return ResponseEntity.status(200).body(appUserService.connectionList(user));

		return ResponseEntity.status(200).body(connectionService.connections(user, Status.PENDING, true));
	}

}
