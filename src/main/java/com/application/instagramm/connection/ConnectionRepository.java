package com.application.instagramm.connection;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.application.instagramm.user.AppUser;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection, ConnectionId> {
	List<Connection> findByAppUserAndStatus(AppUser appUser, Status status);

	List<Connection> findByFriendAndStatus(AppUser friend, Status status);

}
