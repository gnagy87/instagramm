package com.application.instagramm.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
	private AppUser appUser;

	public UserPrincipal(AppUser appUser) {
		this.appUser = appUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> role = new ArrayList<>();
		role.add(new SimpleGrantedAuthority(this.appUser.getRole().toString()));
		return role;
	}

	@Override
	public String getPassword() {
		return this.appUser.getPassword();
	}

	@Override
	public String getUsername() {
		return this.appUser.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.appUser.isEnabled();
	}

}
