package com.application.instagramm.comment;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.application.instagramm.post.Post;
import com.application.instagramm.user.AppUser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String comment;
	private Timestamp createdAt;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private AppUser appUser;

	@ManyToOne
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	public Comment(String comment) {
		this.comment = comment;
		this.createdAt = new Timestamp(System.currentTimeMillis());
	}
}
