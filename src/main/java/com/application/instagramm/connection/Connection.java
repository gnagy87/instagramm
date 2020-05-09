package com.application.instagramm.connection;

import java.sql.Timestamp;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.application.instagramm.user.AppUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "connections")
public class Connection {

	@EmbeddedId
	private ConnectionId id = new ConnectionId();

	@ManyToOne
	@MapsId("appUserId")
	private AppUser appUser;
	@ManyToOne
	@MapsId("invitedUserId")
	private AppUser invitedUser;
	private Timestamp requestSent;
	@Enumerated(EnumType.STRING)
	private Status status;
	private Timestamp responseDate;

}
