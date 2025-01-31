package com.pricewagon.pricewagon.domain.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pricewagon.pricewagon.domain.user.converter.UserConverter;
import com.pricewagon.pricewagon.domain.user.dto.UserRequestDTO;
import com.pricewagon.pricewagon.domain.user.dto.UserResponseDTO;
import com.pricewagon.pricewagon.domain.user.entity.User;
import com.pricewagon.pricewagon.domain.user.service.EmailService;
import com.pricewagon.pricewagon.domain.user.service.UserCommandService;
import com.pricewagon.pricewagon.global.config.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "3. [회원로직]", description = "회원 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
	private final UserCommandService userCommandService;
	private final EmailService emailService;

	// 회원가입 API
	@Operation(summary = "회원 가입", description = "회원 가입 요청을 처리")
	@PostMapping("/join")
	public ResponseEntity<UserResponseDTO.JoinResultDTO> join(
		@Valid @RequestBody UserRequestDTO.JoinDto request
	) {
		User user = userCommandService.joinUser(request);
		UserResponseDTO.JoinResultDTO response = UserConverter.joinResult(user);
		return ResponseEntity.ok(response);
	}

	// 로그인 API
	@Operation(summary = "로그인", description = "일반 로그인")
	@PostMapping("/login")
	public ResponseEntity<UserResponseDTO.loginResultDTO> login(
		@Valid @RequestBody UserRequestDTO.loginDto request
	) {
		UserResponseDTO.loginResultDTO loginResultDTO = userCommandService.loginUser(request);
		return ResponseEntity.ok(loginResultDTO);
	}

	//이메일 인증 API
	@Operation(summary = "이메일 인증", description = "이메일 인증하기")
	@PostMapping("/email")
	public ResponseEntity<UserResponseDTO.emailResultDTO> sendEmail(
		@Valid @RequestBody UserRequestDTO.emailRequestDTO request) {
		String authCode = emailService.joinEmail(request.getAccount());
		return ResponseEntity.ok(UserConverter.toEmailResult(authCode));
	}

	//아이디 중복 확인 API
	@Operation(summary = "아이디 중복 확인 API", description = "아이디 중복 확인하기")
	@PostMapping("/checkEmail")
	public ResponseEntity<UserResponseDTO.emailCheckDTO> checkEmail(
		@Valid @RequestBody UserRequestDTO.checkEmailDTO request) {
		UserResponseDTO.emailCheckDTO emailCheckDTO = userCommandService.checkEmail(request);
		return ResponseEntity.ok(emailCheckDTO);
	}

	// 인증번호 확인 API
	@Operation(summary = "인증번호 확인 API", description = "사용자가 입력한 번호가 인증번호가 맞는지 확인")
	@PostMapping("/valid")
	public ResponseEntity<UserResponseDTO.validNumDTO> validNumCheck(
		@Valid @RequestBody UserRequestDTO.validNumDTO request) {
		Boolean checked = emailService.validCheck(request);
		return ResponseEntity.ok(UserConverter.toValidCheck(checked));
	}

	//카카오 소셜 로그인 API
	@Operation(summary = "카카오 소셜 로그인", description = "카카오 소셜 로그인")
	@GetMapping("/auth/login/kakao")
	public ResponseEntity<UserResponseDTO.loginResultDTO> kakaoLogin(@RequestParam("code") String accessCode,
		HttpServletResponse httpServletResponse) {
		UserResponseDTO.loginResultDTO result = userCommandService.oAuthKakaoLogin(accessCode, httpServletResponse);
		return ResponseEntity.ok(result);
	}

	//네이버 소셜 로그인 API
	@Operation(summary = "네이버 소셜 로그인", description = "네이버 소셜 로그인")
	@GetMapping("/auth/login/naver")
	public ResponseEntity<UserResponseDTO.loginResultDTO> naverLogin(@RequestParam("code") String accessCode,
		HttpServletResponse httpServletResponse) {
		UserResponseDTO.loginResultDTO result = userCommandService.oAuthNaverLogin(accessCode, httpServletResponse);
		return ResponseEntity.ok(result);
	}

	//마이페이지 조회
	@Operation(summary = "마이페이지 조회", description = "이름, 이메일 값 조회")
	@GetMapping("/mypage")
	public ResponseEntity<UserResponseDTO.myPageResultDTO> mypage(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		UserResponseDTO.myPageResultDTO result = userCommandService.mypage(userDetails);
		return ResponseEntity.ok(result);
	}

}
