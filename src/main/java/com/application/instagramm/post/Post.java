package com.application.instagramm.post;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.application.instagramm.comment.Comment;
import com.application.instagramm.user.AppUser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String description;
	private Timestamp createdAt;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private AppUser appUser;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.LAZY)
	private List<Comment> comments;

	public Post(String title, String description) {
		this.title = title;
		this.description = description;
		this.createdAt = new Timestamp(System.currentTimeMillis());
		this.comments = new ArrayList<>();
	}
}
