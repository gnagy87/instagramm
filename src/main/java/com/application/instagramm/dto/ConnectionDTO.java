package com.application.instagramm.dto;

import java.sql.Timestamp;

import com.application.instagramm.connection.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDTO {
	private AppUserDTO user;
	private Status status;
	private Timestamp date;

}
