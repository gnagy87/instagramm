package com.application.instagramm.connection;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ConnectionId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long appUserId;
	private Long friendId;
}
