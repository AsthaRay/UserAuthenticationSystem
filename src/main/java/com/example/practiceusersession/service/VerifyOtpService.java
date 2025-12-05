package com.example.practiceusersession.service;

import com.example.practiceusersession.dto.ResendOtpRequestDto;
import com.example.practiceusersession.dto.ResendOtpResponseDto;
import com.example.practiceusersession.dto.VerifyOtpRequestDto;
import com.example.practiceusersession.exception.*;
import com.example.practiceusersession.model.User;
import com.example.practiceusersession.model.VerifyOtp;
import com.example.practiceusersession.respository.UserRepository;
import com.example.practiceusersession.security.JwtUtil;
import com.example.practiceusersession.respository.VerifyOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class VerifyOtpService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerifyOtpRepository verifyOtpRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;  // add your JWT utility class

    // Resend OTP remains the same
    public ResendOtpResponseDto resendOtp(ResendOtpRequestDto resendRequest) throws ResendEmailNotFoundException {
        Optional<User> user = userRepository.findByEmail(resendRequest.getRegisteredEmail());
        if (user.isEmpty()) {
            throw new ResendEmailNotFoundException("Provided email is not present in the system");
        }

        List<VerifyOtp> existingOtpRecords = verifyOtpRepository.findInvalidOtpsByEmail(
                resendRequest.getRegisteredEmail(),
                resendRequest.getType());

        existingOtpRecords.forEach(record -> record.setIsExpired(true));
        if (!existingOtpRecords.isEmpty()) {
            verifyOtpRepository.saveAll(existingOtpRecords);
        }

        String newOtp = String.valueOf(1000 + new Random().nextInt(9000));
        Long newOtpExpiry = System.currentTimeMillis() + (5 * 60 * 1000);
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

        emailService.sendOtp(resendRequest.getRegisteredEmail(), newOtp);

        return ResendOtpResponseDto.builder()
                .otpSessionID(newOTPSessionID)
                .expireIn(newOtpExpiry)
                .type(resendRequest.getType())
                .build();
    }

    // Updated verifyOtp to return JWT token
  /*  public String verifyOtp(VerifyOtpRequestDto requestDto)
            throws OtpExpiredException,
            OtpAlreadyBeenUsedException,
            WrongOtpException,
            InvalidOtpVerificationException {

        Optional<VerifyOtp> optionalFetchedOtp = verifyOtpRepository.findByOtpSessionId(requestDto.getOtp());
        if(optionalFetchedOtp.isEmpty()){
            throw new InvalidOtpVerificationException("Invalid OTP verification request, please try resending the OTP!");
        }

        VerifyOtp fetchedOtp = optionalFetchedOtp.get();
        if(fetchedOtp.getIsExpired() || fetchedOtp.getExpireIn() <= System.currentTimeMillis()){
            throw new OtpExpiredException("Your OTP has expired, please try resending the OTP!");
        }

        if(fetchedOtp.getIsUsed()){
            throw new OtpAlreadyBeenUsedException("Given OTP has already been used, please try resending the OTP!");
        }

        if(!fetchedOtp.getOtp().equals(requestDto.getOtp())){
            throw new WrongOtpException("You entered wrong OTP, please try again");
        }

        // Mark OTP as used
        fetchedOtp.setIsUsed(true);
        verifyOtpRepository.save(fetchedOtp);

        // Generate JWT token for user
        User user = userRepository.findByEmail(fetchedOtp.getEmail())
                .orElseThrow(() -> new InvalidOtpVerificationException("User not found!"));

        return jwtUtil.generateToken(user.getEmail());  // returns JWT token
    }*/
    public String verifyOtp(VerifyOtpRequestDto requestDto)
            throws OtpExpiredException,
            OtpAlreadyBeenUsedException,
            WrongOtpException {

        // 1. Fetch OTP using email + otp
        Optional<VerifyOtp> optionalFetchedOtp =
                verifyOtpRepository.findByEmailAndOtp(requestDto.getEmail(), requestDto.getOtp());

        if (optionalFetchedOtp.isEmpty()) {
            throw new WrongOtpException("You entered wrong OTP, please try again");
        }

        VerifyOtp fetchedOtp = optionalFetchedOtp.get();

        // 2. Check expiry
        if (fetchedOtp.getIsExpired() || fetchedOtp.getExpireIn() <= System.currentTimeMillis()) {
            throw new OtpExpiredException("Your OTP has expired, please try resending the OTP!");
        }

        // 3. Check if already used
        if (fetchedOtp.getIsUsed()) {
            throw new OtpAlreadyBeenUsedException("Given OTP has already been used, please try resending the OTP!");
        }

        // 4. Mark OTP used
        fetchedOtp.setIsUsed(true);
        verifyOtpRepository.save(fetchedOtp);

        // 5. Fetch user to generate JWT
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new WrongOtpException("User not found!"));

        // 6. Generate JWT token
        return jwtUtil.generateToken(user.getEmail());
    }

}
