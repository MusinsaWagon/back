package com.pricewagon.pricewagon.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pricewagon.pricewagon.domain.user.Converter.UserConverter;
import com.pricewagon.pricewagon.domain.user.dto.CustomUserInfoDto;
import com.pricewagon.pricewagon.domain.user.dto.UserRequestDTO;
import com.pricewagon.pricewagon.domain.user.dto.UserResponseDTO;
import com.pricewagon.pricewagon.domain.user.entity.User;
import com.pricewagon.pricewagon.domain.user.repository.UserRepository;
import com.pricewagon.pricewagon.global.config.security.JwtUtil;
import com.pricewagon.pricewagon.global.error.exception.CustomException;
import com.pricewagon.pricewagon.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	public User joinUser(UserRequestDTO.JoinDto request) {
		// 이미 존재하는 사용자 계정 처리
		userRepository.findByAccount(request.getAccount())
			.ifPresent(user -> {
				throw new CustomException(ErrorCode.MEMBER_ALREADY_EXIST);
			});

		// 새 사용자 생성
		User newUser = UserConverter.toUser(request);
		newUser.encodedPassword(passwordEncoder.encode(request.getPassword()));

		// 사용자 저장
		return userRepository.save(newUser);
	}

	@Override
	public UserResponseDTO.loginResultDTO loginUser(UserRequestDTO.loginDto request) {

		User user = userRepository.findByAccount(request.getAccount())
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}
		CustomUserInfoDto info = UserConverter.toCustomUserInfoDto(user);

		String accessToken = jwtUtil.createAccessToken(info);
		return UserConverter.loginResult(user, accessToken);
	}
}
