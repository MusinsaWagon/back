package com.pricewagon.pricewagon.domain.user.service;

import com.pricewagon.pricewagon.apiPayload.code.status.ErrorStatus;
import com.pricewagon.pricewagon.apiPayload.exception.handler.UserHandler;
import com.pricewagon.pricewagon.domain.user.Converter.UserConverter;
import com.pricewagon.pricewagon.domain.user.dto.UserRequestDTO;
import com.pricewagon.pricewagon.domain.user.dto.UserResponseDTO;
import com.pricewagon.pricewagon.domain.user.entity.User;
import com.pricewagon.pricewagon.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User joinUser (UserRequestDTO.JoinDto request){
        if (userRepository.findByAccount(request.getAccount()).isPresent()){
            throw new UserHandler(ErrorStatus.MEMBER_ALREADY_EXIST);
        } // 이미 존제하는 유저면 에러 반환
        User newUser = UserConverter.toUser(request);
        newUser.encodedPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(newUser);
    }

}
