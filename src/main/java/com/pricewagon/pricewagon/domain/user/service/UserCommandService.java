package com.pricewagon.pricewagon.domain.user.service;

import com.pricewagon.pricewagon.domain.user.dto.UserRequestDTO;
import com.pricewagon.pricewagon.domain.user.dto.UserResponseDTO;
import com.pricewagon.pricewagon.domain.user.entity.User;
import com.pricewagon.pricewagon.global.config.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletResponse;

public interface UserCommandService {
	User joinUser(UserRequestDTO.JoinDto request);

	UserResponseDTO.loginResultDTO loginUser(UserRequestDTO.loginDto request);

	UserResponseDTO.emailCheckDTO checkEmail(UserRequestDTO.checkEmailDTO request);

	UserResponseDTO.loginResultDTO oAuthKakaoLogin(String accessCode, HttpServletResponse httpServletResponse);

	UserResponseDTO.loginResultDTO oAuthNaverLogin(String accessCode, HttpServletResponse httpServletResponse);

	UserResponseDTO.myPageResultDTO mypage(CustomUserDetails userDetails);
}
