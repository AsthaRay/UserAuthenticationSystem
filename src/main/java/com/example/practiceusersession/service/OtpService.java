package com.example.practiceusersession.service;

import com.example.practiceusersession.model.VerifyOtp;
import com.example.practiceusersession.model.enums.OtpType;
import com.example.practiceusersession.respository.VerifyOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.random.RandomGenerator;

@Service
public class OtpService {

    @Autowired
    private VerifyOtpRepository verifyOtpRepository;

    @Autowired
    private EmailService emailService;

    public String generateAndSendOtp(String email, OtpType type) {

        String otp = String.valueOf(RandomGenerator.getDefault().nextInt(1000, 10000));
        Long expiry = System.currentTimeMillis() + (5 * 60 * 1000);
        String otpSessionId = UUID.randomUUID().toString();

        VerifyOtp otpRecord = VerifyOtp.builder()
                .email(email)
                .otp(otp)
                .type(type)
                .isUsed(false)
                .isExpired(false)
                .otpSessionId(otpSessionId)
                .expireIn(expiry)
                .build();

        verifyOtpRepository.save(otpRecord);

        // ⬅⬅ SEND EMAIL
        emailService.sendOtp(email, otp);

        return otpSessionId;
    }
}
