package com.application.instagramm.connection;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.instagramm.dto.AppUserDTO;
import com.application.instagramm.dto.ConnectionDTO;
import com.application.instagramm.user.AppUser;

@Service
public class ConnectionServiceImpl implements ConnectionService {
	private ConnectionRepository connectionRepository;

	@Autowired
	public ConnectionServiceImpl(ConnectionRepository connectionRepository) {
		this.connectionRepository = connectionRepository;
	}

	@Override
	public List<ConnectionDTO> connections(AppUser appUser, Status status, boolean friendOf) {
		List<ConnectionDTO> connections = new ArrayList<>();
		return connectionInitializer(connections, appUser, status, friendOf);

	}

	private List<ConnectionDTO> connectionInitializer(List<ConnectionDTO> connections, AppUser appUser, Status status,
			boolean friendOf) {
		List<ConnectionDTO> result = connections;
		List<Connection> query = new ArrayList<>();
		if (!friendOf) {
			query = connectionRepository.findByAppUserAndStatus(appUser, status);
		} else {
			query = connectionRepository.findByFriendAndStatus(appUser, status);
		}
		for (Connection connection : query) {
			AppUserDTO userDTO = new AppUserDTO();
			if (!friendOf) {
				userDTO.setId(connection.getFriend().getId());
				userDTO.setFirstname(connection.getFriend().getFirstName());
				userDTO.setLastname(connection.getFriend().getLastName());
				userDTO.setEmail(connection.getFriend().getEmail());
			} else {
				userDTO.setId(connection.getAppUser().getId());
				userDTO.setFirstname(connection.getAppUser().getFirstName());
				userDTO.setLastname(connection.getAppUser().getLastName());
				userDTO.setEmail(connection.getAppUser().getEmail());
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
