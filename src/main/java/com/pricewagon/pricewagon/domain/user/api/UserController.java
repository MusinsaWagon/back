package com.pricewagon.pricewagon.domain.user.api;

import com.pricewagon.pricewagon.apiPayload.ApiResponse;
import com.pricewagon.pricewagon.domain.user.Converter.UserConverter;
import com.pricewagon.pricewagon.domain.user.dto.UserRequestDTO;
import com.pricewagon.pricewagon.domain.user.dto.UserResponseDTO;
import com.pricewagon.pricewagon.domain.user.entity.User;
import com.pricewagon.pricewagon.domain.user.service.UserCommandService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vi/Users")
public class UserController {
    private final UserCommandService userCommandService;

    //회원가입 api
    @PostMapping("/join")
    public ApiResponse<UserResponseDTO.JoinResultDTO> join (@RequestBody @Valid UserRequestDTO. JoinDto request){
        User user = userCommandService.joinUser(request);
        return ApiResponse.onSuccess(UserConverter.joinResult(user));
    }

    //로그인 api
//    @PostMapping("/login")
//    public ApiResponse<UserResponseDTO.loginResultDTO> login (@RequestBody @Valid UserRequestDTO. loginDTO request){
//        UserResponseDTO.loginResultDTO loginResultDto = userCommandService.login(request);
//        return ApiResponse.onSuccess(loginResultDto);
//    }

}
