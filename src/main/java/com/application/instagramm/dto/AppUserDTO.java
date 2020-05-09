package com.application.instagramm.dto;

import com.application.instagramm.user.AppUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {
	private Long id;
	private String username;
	private String firstname;
	private String lastname;
	private String email;
	
	public AppUserDTO(AppUser appUser) {
		this.id = appUser.getId();
		this.username = appUser.getUsername();
		this.firstname = appUser.getFirstName();
		this.lastname = appUser.getLastName();
		this.email = appUser.getEmail();
	}

}
