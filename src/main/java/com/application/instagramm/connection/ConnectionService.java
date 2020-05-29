package com.application.instagramm.connection;

import java.util.List;

import com.application.instagramm.dto.ConnectionDTO;
import com.application.instagramm.user.AppUser;

public interface ConnectionService {
	List<ConnectionDTO> getConnections(AppUser appUser, Status status, boolean friendOf);

	void saveConnection(Connection connection);

}
