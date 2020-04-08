package com.application.instagramm.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long> {
	
	Optional<AppUser> findAppUserByUsername(String username);
	
}
