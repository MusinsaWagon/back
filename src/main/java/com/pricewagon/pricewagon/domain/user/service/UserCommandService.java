package com.pricewagon.pricewagon.domain.user.service;

import com.pricewagon.pricewagon.domain.user.dto.UserRequestDTO;
import com.pricewagon.pricewagon.domain.user.dto.UserResponseDTO;
import com.pricewagon.pricewagon.domain.user.entity.User;

public interface UserCommandService {
	User joinUser(UserRequestDTO.JoinDto request);

	UserResponseDTO.loginResultDTO loginUser(UserRequestDTO.loginDto request);

	UserResponseDTO.emailCheckDTO checkEmail(UserRequestDTO.checkEmailDTO request);

}
