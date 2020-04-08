package com.application.instagramm.dto;

import java.sql.Timestamp;

import com.application.instagramm.connection.Status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConnectionDTO {
	private AppUserDTO user;
	private Status status;
	private Timestamp date;

	public ConnectionDTO(AppUserDTO user, Status status, Timestamp date) {
		this.user = user;
		this.status = status;
		this.date = date;
	}

}
