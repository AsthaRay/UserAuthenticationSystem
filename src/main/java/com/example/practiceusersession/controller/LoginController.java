package com.example.practiceusersession.controller;

import com.example.practiceusersession.dto.GenericResponseDto;
import com.example.practiceusersession.dto.LoginRequestDto;
import com.example.practiceusersession.dto.LoginResponseDto;
import com.example.practiceusersession.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<GenericResponseDto<LoginResponseDto>> doLogin(
            @RequestBody LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(
                GenericResponseDto.<LoginResponseDto>builder()
                        .error(false)
                        .message("Welcome User, Please enter OTP to access the system.")
                        .data(loginService.doLogin(loginRequestDto))
                        .build(),
                HttpStatus.OK);
    }
}
