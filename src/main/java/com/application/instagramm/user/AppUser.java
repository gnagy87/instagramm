package com.application.instagramm.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.application.instagramm.comment.Comment;
import com.application.instagramm.connection.Connection;
import com.application.instagramm.post.Post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String firstName;
	@Column(nullable = false)
	private String lastName;
	@Enumerated(EnumType.STRING)
	private Role role;
	private boolean isEnabled;
	@Column(name = "created_at")
	private Timestamp createdAt;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "appUser", fetch = FetchType.LAZY)
	private List<Post> posts;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "appUser", fetch = FetchType.LAZY)
	private List<Comment> comments;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "appUser", fetch = FetchType.LAZY)
	private Set<Connection> connections;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "invitedUser", fetch = FetchType.LAZY)
	private Set<Connection> connectionOf;

	public AppUser(String username, String password, String email, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = Role.USER;
		this.isEnabled = false;
		this.createdAt = new Timestamp(System.currentTimeMillis());
		this.posts = new ArrayList<>();
		this.comments = new ArrayList<>();
		this.connections = new HashSet<>();
		this.connectionOf = new HashSet<>();
	}
	
	public void addConnection(Connection connection) {
		this.getConnections().add(connection);
	}
	
	public void addConnectionOf(Connection connection) {
		this.getConnectionOf().add(connection);
	}
}
