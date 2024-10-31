package com.pricewagon.pricewagon.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import com.pricewagon.pricewagon.domain.alarm.entity.Alarm;
import com.pricewagon.pricewagon.domain.common.UserAuditEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends UserAuditEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Comment("아이디")
	@Column(columnDefinition = "varchar(20)", nullable = false, unique = true)
	private String account;

	@Comment("비밀번호")
	@Column(columnDefinition = "varchar(20)", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	public void encodedPassword(String password){
		this.password = password;
	}

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Alarm> alarms = new ArrayList<>();

}
