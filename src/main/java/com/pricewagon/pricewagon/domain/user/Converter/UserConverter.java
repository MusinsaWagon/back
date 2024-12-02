package com.pricewagon.pricewagon.domain.user.Converter;

import com.pricewagon.pricewagon.domain.user.dto.CustomUserInfoDto;
import com.pricewagon.pricewagon.domain.user.dto.UserRequestDTO;
import com.pricewagon.pricewagon.domain.user.dto.UserResponseDTO;
import com.pricewagon.pricewagon.domain.user.entity.User;

public class UserConverter {
	public static User toUser(UserRequestDTO.JoinDto request) {
		return User.builder()
			.account(request.getAccount())
			.password(request.getPassword())
			.role(request.getRole())
			.build();
	}

	// 회원가입 결과값
	public static UserResponseDTO.JoinResultDTO joinResult(User user) {
		return UserResponseDTO.JoinResultDTO.builder()
			.memberId(user.getId())
			.account(user.getAccount())
			.createdAt(user.getCreatedAt())
			.build();
	}

	public static UserResponseDTO.loginResultDTO loginResult(User user, String accessToken) {
		return UserResponseDTO.loginResultDTO.builder()
			.memberId(user.getId())
			.account(user.getAccount())
			.createdAt(user.getCreatedAt())
			.accessToken(accessToken)
			.build();
	}

	public static CustomUserInfoDto toCustomUserInfoDto(User user) {
		return CustomUserInfoDto.builder()
			.userId(user.getId())
			.email(user.getAccount())
			.role(user.getRole())
			.build();
	}

	public static UserResponseDTO.emailResultDTO toEmailResult(String authCode) {
		return UserResponseDTO.emailResultDTO.builder()
			.validCode(Integer.valueOf(authCode)) // 생성된 인증번호
			.build();
	}

	public static UserResponseDTO.emailCheckDTO toCheckResult(UserRequestDTO.checkEmailDTO request,
		boolean isDuplicated) {
		return UserResponseDTO.emailCheckDTO.builder()
			.isDuplicated(isDuplicated)
			.email(request.getEmail())
			.build();
	}

	public static UserResponseDTO.validNumDTO toValidCheck(Boolean checked) {
		return UserResponseDTO.validNumDTO.builder()
			.isValid(checked)
			.build();
	}

}
