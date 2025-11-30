package com.example.practiceusersession.service;

import com.example.practiceusersession.dto.LoginRequestDto;
import com.example.practiceusersession.dto.LoginResponseDto;
import com.example.practiceusersession.dto.RegisterRequestDto;
import com.example.practiceusersession.dto.RegisterResponseDto;
import com.example.practiceusersession.exception.LoginWrongCredentialsException;
import com.example.practiceusersession.model.User;
import com.example.practiceusersession.model.enums.OtpType;
import com.example.practiceusersession.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    public RegisterResponseDto doRegister(RegisterRequestDto registerRequest) {

        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .build();

        userRepository.save(user);

        String otpSessionId = otpService.generateAndSendOtp(
                registerRequest.getEmail(),
                OtpType.OTP_TYPE_REGISTER
        );

        return RegisterResponseDto.builder()
                .type(OtpType.OTP_TYPE_REGISTER)
                .expireIn(System.currentTimeMillis() + (5 * 60 * 1000))
                .tempSessionId(UUID.fromString(otpSessionId))
                .build();
    }
}
