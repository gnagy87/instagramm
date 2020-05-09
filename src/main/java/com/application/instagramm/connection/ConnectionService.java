package com.application.instagramm.connection;

import java.util.List;

import com.application.instagramm.dto.ConnectionDTO;
import com.application.instagramm.dto.StatusMessageDTO;
import com.application.instagramm.exceptions.ConnectionException;
import com.application.instagramm.exceptions.UserException;
import com.application.instagramm.user.AppUser;

public interface ConnectionService {

	void saveConnection(Connection connection);
	
	StatusMessageDTO friendRequestMaker(AppUser appUser, AppUser invitedUser) throws ConnectionException;
	
	StatusMessageDTO acceptFriendRequest(AppUser appUser, AppUser invitedUser) throws ConnectionException;

}
