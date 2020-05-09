package com.application.instagramm.connection;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.instagramm.dto.AppUserDTO;
import com.application.instagramm.dto.ConnectionDTO;
import com.application.instagramm.dto.StatusMessageDTO;
import com.application.instagramm.exceptions.ConnectionException;
import com.application.instagramm.exceptions.UserException;
import com.application.instagramm.user.AppUser;
import com.application.instagramm.user.AppUserService;

@Service
public class ConnectionServiceImpl implements ConnectionService {
	private ConnectionRepository connectionRepository;
	private AppUserService appUserService;

	@Autowired
	public ConnectionServiceImpl(ConnectionRepository connectionRepository, AppUserService appUserService) {
		this.connectionRepository = connectionRepository;
		this.appUserService = appUserService;
	}
	
	@Override
	public void saveConnection(Connection connection) {
		connectionRepository.save(connection);
	}

	@Override
	public StatusMessageDTO friendRequestMaker(AppUser appUser, AppUser invitedUser) throws ConnectionException {
		if (!connectionRepository.findByAppUserAndInvitedUserAndStatus(appUser, invitedUser, Status.ACCEPTED).isPresent() &&
			!connectionRepository.findByAppUserAndInvitedUserAndStatus(appUser, invitedUser, Status.PENDING).isPresent() &&
			!connectionRepository.findByAppUserAndInvitedUserAndStatus(invitedUser, appUser, Status.ACCEPTED).isPresent() &&
			!connectionRepository.findByAppUserAndInvitedUserAndStatus(invitedUser, appUser, Status.PENDING).isPresent()) {
			Connection connection = new Connection();
			connection.setAppUser(appUser);
			connection.setInvitedUser(invitedUser);
			connection.setStatus(Status.PENDING);
			connection.setRequestSent(new Timestamp(System.currentTimeMillis()));
			saveConnection(connection);
			
			return new StatusMessageDTO("200","Request has been sent");
		} else {
			throw new ConnectionException("Request can not be sent!");
		}
	}

	@Override
	public StatusMessageDTO acceptFriendRequest(AppUser appUser, AppUser invitedUser) throws ConnectionException {
		Optional<Connection> pendingConnection = connectionRepository.findByAppUserAndInvitedUserAndStatus(appUser, invitedUser, Status.PENDING);
		if (pendingConnection.isPresent()) {
			Connection acceptedConnection = pendingConnection.get();
			acceptedConnection.setStatus(Status.ACCEPTED);
			acceptedConnection.setRequestSent(new Timestamp(System.currentTimeMillis()));
			saveConnection(acceptedConnection);
			
			return new StatusMessageDTO("200", "Request was accepted");
		} else {
			throw new ConnectionException("Request was not accepted!");
		}
	}
}
