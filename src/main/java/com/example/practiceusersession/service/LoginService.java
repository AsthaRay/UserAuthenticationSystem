package com.example.practiceusersession.service;

import com.example.practiceusersession.dto.LoginRequestDto;
import com.example.practiceusersession.dto.LoginResponseDto;
import com.example.practiceusersession.exception.LoginWrongCredentialsException;
import com.example.practiceusersession.model.User;
import com.example.practiceusersession.model.enums.OtpType;
import com.example.practiceusersession.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    public LoginResponseDto doLogin(LoginRequestDto loginRequest) throws LoginWrongCredentialsException {

        Optional<User> optionalUser = userRepository.findByEmailAndPassword(
                loginRequest.getEmail(), loginRequest.getPassword());

        if (optionalUser.isEmpty()) {
            throw new LoginWrongCredentialsException(
                    "You've entered wrong email or password, please verify and try again");
        }

        User user = optionalUser.get();

        // ⬅ Generate OTP + Save in DB + Send email
        String otpSessionId = otpService.generateAndSendOtp(
                user.getEmail(),
                OtpType.OTP_TYPE_2FA
        );

        long expiry = System.currentTimeMillis() + (5 * 60 * 1000); // 5 min

        return LoginResponseDto.builder()
                .type(OtpType.OTP_TYPE_2FA)
                .expireIn(expiry)
                .tempSessionId(UUID.fromString(otpSessionId))
                .build();
    }
}
