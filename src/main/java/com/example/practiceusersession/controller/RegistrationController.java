package com.example.practiceusersession.controller;

import com.example.practiceusersession.dto.GenericResponseDto;
import com.example.practiceusersession.dto.RegisterRequestDto;
import com.example.practiceusersession.dto.RegisterResponseDto;
import com.example.practiceusersession.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<GenericResponseDto<RegisterResponseDto>> doRegister(
            @RequestBody RegisterRequestDto requestDto) {

        return new ResponseEntity<>(
                GenericResponseDto.<RegisterResponseDto>builder()
                        .error(false)
                        .message("Registered successfully, please check your email inbox and verify the account using sent OTP.")
                        .data(registrationService.doRegister(requestDto))
                        .build(),
                HttpStatus.CREATED
        );
    }
}
