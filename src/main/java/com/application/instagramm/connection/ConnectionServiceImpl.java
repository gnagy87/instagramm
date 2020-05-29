package com.application.instagramm.connection;

import java.util.ArrayList;
import java.util.List;

import com.application.instagramm.mappers.AppUserMapper;
import org.springframework.stereotype.Service;

import com.application.instagramm.dto.AppUserDTO;
import com.application.instagramm.dto.ConnectionDTO;
import com.application.instagramm.user.AppUser;

@Service
public class ConnectionServiceImpl implements ConnectionService {
	private ConnectionRepository connectionRepository;
	private final AppUserMapper appUserMapper;

	public ConnectionServiceImpl(ConnectionRepository connectionRepository, AppUserMapper appUserMapper) {
		this.connectionRepository = connectionRepository;
		this.appUserMapper = appUserMapper;
	}

	@Override
	public List<ConnectionDTO> getConnections(AppUser appUser, Status status, boolean friendOf) {
		List<ConnectionDTO> connections = new ArrayList<>();
		return connectionInitializer(connections, appUser, status, friendOf);

	}

	private List<ConnectionDTO> connectionInitializer(List<ConnectionDTO> connections, AppUser appUser, Status status,
			boolean friendOf) {
		List<ConnectionDTO> result = connections;
		List<Connection> query;
		if (!friendOf) {
			query = connectionRepository.findByAppUserAndStatus(appUser, status);
		} else {
			query = connectionRepository.findByFriendAndStatus(appUser, status);
		}
		for (Connection connection : query) {
			AppUserDTO userDTO;
			if (!friendOf) {
				userDTO = appUserMapper.appUserToAppUserDTO(connection.getFriend());
			} else {
				userDTO = appUserMapper.appUserToAppUserDTO(connection.getAppUser());
			}
			ConnectionDTO connectionDTO = new ConnectionDTO(userDTO, connection.getStatus(),
					connection.getResponseDate());
			result.add(connectionDTO);
		}
		if (friendOf) {
			return connectionInitializer(result, appUser, status, false);
		} else {
			return result;
		}
	}

	@Override
	public void saveConnection(Connection connection) {
		connectionRepository.save(connection);

	}
}
