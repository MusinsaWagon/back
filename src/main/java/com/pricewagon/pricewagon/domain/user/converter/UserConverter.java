package com.pricewagon.pricewagon.domain.user.converter;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.pricewagon.pricewagon.domain.user.dto.CustomUserInfoDto;
import com.pricewagon.pricewagon.domain.user.dto.KakaoDTO;
import com.pricewagon.pricewagon.domain.user.dto.NaverDTO;
import com.pricewagon.pricewagon.domain.user.dto.UserRequestDTO;
import com.pricewagon.pricewagon.domain.user.dto.UserResponseDTO;
import com.pricewagon.pricewagon.domain.user.entity.User;
import com.pricewagon.pricewagon.domain.user.entity.UserRole;

public class UserConverter {
	public static User toUser(UserRequestDTO.JoinDto request) {
		return User.builder()
			.account(request.getAccount())
			.password(request.getPassword())
			.role(request.getRole())
			.build();
	}

	public static User toUser(KakaoDTO.KakaoProfile kakaoProfile, PasswordEncoder passwordEncoder) {
		return User.builder()
			.account(kakaoProfile.getKakao_account().getEmail())
			.password(passwordEncoder.encode("kakao"))
			.role(UserRole.USER)
			.build();
	}

	public static User toUser(NaverDTO.UserInfo naverProfile, PasswordEncoder passwordEncoder) {
		return User.builder()
			.account(naverProfile.getResponse().getEmail())
			.password(passwordEncoder.encode("naver"))
			.role(UserRole.USER)
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
			.account(user.getAccount())
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

	public static UserResponseDTO.myPageResultDTO toMyPageResult(User user) {
		return UserResponseDTO.myPageResultDTO.builder()
			.email(user.getAccount())
			.build();
	}

}
