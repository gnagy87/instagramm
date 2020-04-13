package com.application.instagramm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.application.instagramm.user.AppUser;
import com.application.instagramm.user.AppUserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	private AppUserRepository appUserRepository;
	
	@Autowired
	public MyUserDetailsService(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser user = appUserRepository.findAppUserByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("user does not exist!"));
		return new MyUserDetails(user);
	}
}
