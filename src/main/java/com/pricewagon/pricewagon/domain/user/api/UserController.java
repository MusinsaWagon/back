package com.pricewagon.pricewagon.domain.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pricewagon.pricewagon.domain.user.Converter.UserConverter;
import com.pricewagon.pricewagon.domain.user.dto.UserRequestDTO;
import com.pricewagon.pricewagon.domain.user.dto.UserResponseDTO;
import com.pricewagon.pricewagon.domain.user.entity.User;
import com.pricewagon.pricewagon.domain.user.service.UserCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "3. [회원로직]", description = "회원 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vi/Users")
public class UserController {
	private final UserCommandService userCommandService;

	//회원가입 api
	@Operation(summary = "회원 가입", description = "회원 가입 요청을 처리")
	@PostMapping("/join")
	public ResponseEntity<UserResponseDTO.JoinResultDTO> join(
		@Valid @RequestBody UserRequestDTO.JoinDto request
	) {
		User user = userCommandService.joinUser(request);
		UserResponseDTO.JoinResultDTO response = UserConverter.joinResult(user);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "로그인", description = "일반 로그인")
	@PostMapping("/login")
	public ResponseEntity<UserResponseDTO.loginResultDTO> login(
		@Valid @RequestBody UserRequestDTO.loginDto request) {
		UserResponseDTO.loginResultDTO loginResultDTO = userCommandService.login(request);
		return ResponseEntity.ok(response);
	}

	//로그인 api
	//    @PostMapping("/login")
	//    public ApiResponse<UserResponseDTO.loginResultDTO> login (@RequestBody @Valid UserRequestDTO. loginDTO request){
	//        UserResponseDTO.loginResultDTO loginResultDto = userCommandService.login(request);
	//        return ApiResponse.onSuccess(loginResultDto);
	//    }

}
