package com.application.instagramm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionFilterDTO {
	private String username;
	private String status;
	private boolean isInvitedUser;
}
