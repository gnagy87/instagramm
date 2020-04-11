package com.application.instagramm.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
	private AppUserRepository appUserRepository;

	public UserPrincipalDetailsService(AppUserRepository appUSerRepository) {
		this.appUserRepository = appUSerRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new UserPrincipal(appUserRepository.findAppUserByUsername(username).get());
	}

}
