package com.application.instagramm.controllers;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.instagramm.connection.Connection;
import com.application.instagramm.connection.ConnectionService;
import com.application.instagramm.connection.Status;
import com.application.instagramm.dto.ConnectionFilterDTO;
import com.application.instagramm.dto.ErrorMessageDTO;
import com.application.instagramm.dto.StatusMessageDTO;
import com.application.instagramm.exceptions.ConnectionException;
import com.application.instagramm.user.AppUser;
import com.application.instagramm.user.AppUserService;

@RestController
@RequestMapping("/api/connection")
public class ConnectionControllerAPI {
	private ConnectionService connectionService;
	private AppUserService appUserService;
	
	@Autowired
	public ConnectionControllerAPI(ConnectionService connectionService, AppUserService appUserService) {
		this.connectionService = connectionService;
		this.appUserService = appUserService;
	}
	
	@PostMapping("/request/{username}")
	public ResponseEntity sendRequest(@PathVariable String username, HttpServletRequest request) {
		try {
			AppUser appUser = appUserService.findById(appUserService.getUserIdFromToken(appUserService.getToken(request)));
			AppUser invitedUser = appUserService.findByUsername(username);
			return ResponseEntity.status(200).body(connectionService.friendRequestMaker(appUser, invitedUser));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new ErrorMessageDTO("400", e.getMessage()));
		}
	}
	
	@PutMapping("/accept/{username}")
	public ResponseEntity acceptRequest(@PathVariable String username, HttpServletRequest request) {
		try {
			AppUser invitedUser = appUserService.findById(appUserService.getUserIdFromToken(appUserService.getToken(request)));
			AppUser appUser = appUserService.findByUsername(username);
			return ResponseEntity.status(200).body(connectionService.acceptFriendRequest(appUser, invitedUser));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(new ErrorMessageDTO("400", e.getMessage()));
		}
	}
	
	@GetMapping("/list")
	public ResponseEntity getConnections(@RequestBody ConnectionFilterDTO connectionFilterDTO) {
		try {
			return ResponseEntity.status(200).body(appUserService.connectionList(connectionFilterDTO));
		} catch (Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

}
