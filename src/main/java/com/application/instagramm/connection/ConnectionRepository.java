package com.application.instagramm.connection;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.application.instagramm.user.AppUser;

@Repository
public interface ConnectionRepository extends CrudRepository<Connection, ConnectionId> {
	
	List<Connection> findByAppUserAndStatus(AppUser appUser, Status status);
	
	List<Connection> findByInvitedUserAndStatus(AppUser invitedUser, Status status);
	
	Optional<Connection> findByAppUserAndInvitedUserAndStatus(AppUser appUser, AppUser invitedUser, Status status); 

}
