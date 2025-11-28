package com.example.practiceusersession.controller;

import com.example.practiceusersession.dto.GenericResponseDto;
import com.example.practiceusersession.dto.ResendOtpRequestDto;
import com.example.practiceusersession.dto.ResendOtpResponseDto;
import com.example.practiceusersession.dto.VerifyOtpRequestDto;
import com.example.practiceusersession.service.VerifyOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/otp")
public class VerifyOtpController {
    @Autowired
    private VerifyOtpService otpService;

    @PostMapping("/resendOtp")
    public ResponseEntity<GenericResponseDto<ResendOtpResponseDto>> resendOtp(
            @RequestBody ResendOtpRequestDto requestDto) {
        return new ResponseEntity<>(
                GenericResponseDto.<ResendOtpResponseDto>builder()
                        .error(false)
                        .message("OTP sent again!, please check your email inbox and verify the OTP.")
                        .data(otpService.resendOtp(requestDto))
                        .build(),
                HttpStatus.CREATED);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<GenericResponseDto<Object>> verifyOtp(
            @RequestBody VerifyOtpRequestDto requestDto) {
        otpService.verifyOtp(requestDto);
        return new ResponseEntity<>(
                GenericResponseDto.builder()
                        .error(true)
                        .message("Your otp has been verified!")
                        .build(),
                HttpStatus.OK);
    }
}
