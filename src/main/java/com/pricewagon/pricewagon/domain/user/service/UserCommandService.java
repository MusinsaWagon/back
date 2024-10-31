package com.pricewagon.pricewagon.domain.user.service;

import com.pricewagon.pricewagon.domain.user.dto.UserRequestDTO;
import com.pricewagon.pricewagon.domain.user.dto.UserResponseDTO;
import com.pricewagon.pricewagon.domain.user.entity.User;
import com.pricewagon.pricewagon.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface UserCommandService {
    User joinUser (UserRequestDTO.JoinDto request);

}
