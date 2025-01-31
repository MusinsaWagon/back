package com.pricewagon.pricewagon.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pricewagon.pricewagon.domain.user.converter.UserConverter;
import com.pricewagon.pricewagon.domain.user.dto.CustomUserInfoDto;
import com.pricewagon.pricewagon.domain.user.dto.KakaoDTO;
import com.pricewagon.pricewagon.domain.user.dto.NaverDTO;
import com.pricewagon.pricewagon.domain.user.dto.UserRequestDTO;
import com.pricewagon.pricewagon.domain.user.dto.UserResponseDTO;
import com.pricewagon.pricewagon.domain.user.entity.User;
import com.pricewagon.pricewagon.domain.user.repository.UserRepository;
import com.pricewagon.pricewagon.global.config.security.CustomUserDetails;
import com.pricewagon.pricewagon.global.config.security.JwtUtil;
import com.pricewagon.pricewagon.global.config.security.KakaoUtil;
import com.pricewagon.pricewagon.global.config.security.NaverUtil;
import com.pricewagon.pricewagon.global.error.exception.CustomException;
import com.pricewagon.pricewagon.global.error.exception.ErrorCode;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	private final JwtUtil jwtUtil;
	private final KakaoUtil kakaoUtil;
	private final NaverUtil naverUtil;

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

	@Override
	public UserResponseDTO.emailCheckDTO checkEmail(UserRequestDTO.checkEmailDTO request) {
		boolean isDuplicated = userRepository.existsByAccount(request.getEmail());
		return UserConverter.toCheckResult(request, isDuplicated);
	}

	@Override
	public UserResponseDTO.loginResultDTO oAuthKakaoLogin(String accessCode, HttpServletResponse httpServletResponse) {
		KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
		KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);
		String email = kakaoProfile.getKakao_account().getEmail();
		User user = userRepository.findByAccount(email)
			.orElseGet(() -> createNewUser(kakaoProfile));
		String token = jwtUtil.createAccessToken(UserConverter.toCustomUserInfoDto(user));
		return UserConverter.loginResult(user, token);

	}

	@Override
	public UserResponseDTO.loginResultDTO oAuthNaverLogin(String accessCode, HttpServletResponse httpServletResponse) {
		NaverDTO.OAuthToken oAuthToken = naverUtil.requestToken(accessCode);
		NaverDTO.UserInfo userInfo = naverUtil.requestUserInfo(oAuthToken.getAccess_token());
		String email = userInfo.getResponse().getEmail();
		User user = userRepository.findByAccount(email)
			.orElseGet(() -> createNewUser(userInfo));
		String token = jwtUtil.createAccessToken(UserConverter.toCustomUserInfoDto(user));
		return UserConverter.loginResult(user, token);
	}

	@Override
	public UserResponseDTO.myPageResultDTO mypage(CustomUserDetails userDetails) {
		User user = userRepository.findByAccount(userDetails.getUsername())
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
		return UserConverter.toMyPageResult(user);

	}

	private User createNewUser(KakaoDTO.KakaoProfile kakaoProfile) {
		User newUser = UserConverter.toUser(kakaoProfile, passwordEncoder);
		return userRepository.save(newUser);
	}

	private User createNewUser(NaverDTO.UserInfo userInfo) {
		User newUser = UserConverter.toUser(userInfo, passwordEncoder);
		return userRepository.save(newUser);
	}

}
