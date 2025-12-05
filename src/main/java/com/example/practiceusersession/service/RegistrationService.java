package com.example.practiceusersession.service;

import com.example.practiceusersession.dto.RegisterRequestDto;
import com.example.practiceusersession.dto.RegisterResponseDto;
import com.example.practiceusersession.exception.EmailAlreadyExistsException;
import com.example.practiceusersession.exception.InvalidRoleException;
import com.example.practiceusersession.model.Role;
import com.example.practiceusersession.model.User;
import com.example.practiceusersession.model.enums.OtpType;
import com.example.practiceusersession.respository.RoleRepository;
import com.example.practiceusersession.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;   // <-- ADD THIS

    @Autowired
    private OtpService otpService;

    public RegisterResponseDto doRegister(RegisterRequestDto registerRequest) {

        Role userRole = roleRepository.findByName(registerRequest.getRole());
        if (userRole == null) {
            throw new InvalidRoleException("Given role is not valid");
        }

        Optional<User> optionalUser = userRepository.findByEmail(registerRequest.getEmail());
        if (optionalUser.isPresent()) {
            throw new EmailAlreadyExistsException("Email is already exist");
        }


        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .roles(List.of(userRole))   // <-- ROLE ADDED HERE
                .build();

        userRepository.save(user);

        // 3️⃣ Generate OTP after saving user
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
