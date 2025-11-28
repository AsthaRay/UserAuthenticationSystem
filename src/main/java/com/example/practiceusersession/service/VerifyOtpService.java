package com.example.practiceusersession.service;

import com.example.practiceusersession.dto.ResendOtpRequestDto;
import com.example.practiceusersession.dto.ResendOtpResponseDto;
import com.example.practiceusersession.dto.VerifyOtpRequestDto;
import com.example.practiceusersession.exception.*;
import com.example.practiceusersession.model.User;
import com.example.practiceusersession.model.VerifyOtp;
import com.example.practiceusersession.respository.UserRepository;
import com.example.practiceusersession.respository.VerifyOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.random.RandomGenerator;

@Service
public class VerifyOtpService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerifyOtpRepository verifyOtpRepository;

    public ResendOtpResponseDto resendOtp(
            ResendOtpRequestDto resendRequest) throws ResendEmailNotFoundException {
        Optional<User> user = userRepository.findByEmail(resendRequest.getRegisteredEmail());
        if (user.isEmpty()) {
            throw new ResendEmailNotFoundException("Provided email is not present in the system");
        }

        List<VerifyOtp> existingOtpRecords = verifyOtpRepository.findInvalidOtpsByEmail(
                resendRequest.getRegisteredEmail(),
                resendRequest.getType());
        List<VerifyOtp> expiredOtps = existingOtpRecords.stream().peek(
                record -> {
                    record.setIsExpired(true);
                }
        ).toList();
        if (!expiredOtps.isEmpty()) {
            verifyOtpRepository.saveAll(expiredOtps);
        }
        String newOtp = String.valueOf(RandomGenerator.getDefault().nextInt(1000, 10000));
        Long newOtpExpiry = System.currentTimeMillis() + (5 * 60 * 1000); //Expire in 5 minutes
        UUID newOTPSessionID = UUID.randomUUID();

        VerifyOtp newOtpRecord = VerifyOtp.builder()
                .email(resendRequest.getRegisteredEmail())
                .type(resendRequest.getType())
                .otp(newOtp)
                .isExpired(false)
                .isUsed(false)
                .expireIn(newOtpExpiry)
                .otpSessionId(newOTPSessionID.toString())
                .build();

        verifyOtpRepository.save(newOtpRecord);
        return ResendOtpResponseDto.builder()
                .otpSessionID(newOTPSessionID)
                .expireIn(newOtpExpiry)
                .type(resendRequest.getType())
                .build();
    }

    public boolean verifyOtp(VerifyOtpRequestDto requestDto)
            throws OtpExpiredException,
            OtpAlreadyBeenUsedException,
            WrongOtpException,
            InvalidOtpVerificationException {

        Optional<VerifyOtp> optionalFetchedOtp = verifyOtpRepository.findByOtpSessionId(requestDto.getOtpSessionId());
        if(optionalFetchedOtp.isEmpty()){
            throw new InvalidOtpVerificationException("Invalid OTP verification request, please try resending the OTP!");
        }

        VerifyOtp fetchedOtp = optionalFetchedOtp.get();
        if(fetchedOtp.getIsExpired() || fetchedOtp.getExpireIn() <= System.currentTimeMillis()){
            throw new OtpExpiredException("Your OTP has been expired, please try resending the OTP!");
        }

        if(fetchedOtp.getIsUsed()){
            throw new OtpAlreadyBeenUsedException("Given OTP has already been used to verify past request, please try resending the OTP!");
        }

        if(!fetchedOtp.getOtp().equals(requestDto.getOtp())){
            throw new WrongOtpException("You entered wrong OTP, please try again");
        }

        fetchedOtp.setIsUsed(true);
        verifyOtpRepository.save(fetchedOtp);

        return true;
    }
}
