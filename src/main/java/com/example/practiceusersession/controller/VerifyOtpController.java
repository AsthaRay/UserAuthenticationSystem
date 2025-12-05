package com.example.practiceusersession.controller;

import com.example.practiceusersession.dto.*;
import com.example.practiceusersession.service.OtpService;
import com.example.practiceusersession.service.VerifyOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
                        .message("OTP sent again! Please check your email.")
                        .data(otpService.resendOtp(requestDto))
                        .build(),
                HttpStatus.CREATED);
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<GenericResponseDto<Map<String, Object>>> verifyOtp(
            @RequestBody VerifyOtpRequestDto requestDto) {

        String jwtToken = otpService.verifyOtp(requestDto);

        Map<String, Object> data = new HashMap<>();
        data.put("jwtToken", jwtToken);
        data.put("expireIn", System.currentTimeMillis() + (5 * 60 * 1000));

        return new ResponseEntity<>(
                GenericResponseDto.<Map<String, Object>>builder()
                        .error(false)
                        .message("OTP verified successfully!")
                        .data(data)
                        .build(),
                HttpStatus.OK
        );
    }
}
