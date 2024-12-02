package com.pricewagon.pricewagon.domain.user.dto;

import com.pricewagon.pricewagon.domain.user.entity.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserInfoDto {
	private Long userId;
	private String email;
	private UserRole role;
	private String password;
}
