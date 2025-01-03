package com.pricewagon.pricewagon.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Comment;

import com.pricewagon.pricewagon.domain.alarm.entity.Alarm;
import com.pricewagon.pricewagon.domain.common.FullTimeAuditEntity;
import com.pricewagon.pricewagon.domain.fcm.entity.FcmToken;
import com.pricewagon.pricewagon.domain.likes.entity.Likes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class User extends FullTimeAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Comment("아이디")
	@Column(columnDefinition = "varchar(255)", nullable = false, unique = true)
	private String account;

	@Comment("비밀번호")
	@Column(columnDefinition = "varchar(255)", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	public void encodedPassword(String password) {
		this.password = password;
	}

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Alarm> alarms = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FcmToken> fcmTokens = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Likes> likes = new ArrayList<>();

	public void addFcmToken(FcmToken token) {
		this.fcmTokens.add(token);
		token.setUser(this);
	}

	public void removeFcmToken(FcmToken token) {
		this.fcmTokens.remove(token);
		token.setUser(null);
	}

}
