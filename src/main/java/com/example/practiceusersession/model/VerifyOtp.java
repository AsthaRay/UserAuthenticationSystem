package com.example.practiceusersession.model;

import com.example.practiceusersession.model.enums.OtpType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO INCREMENT
    private Long id;
    private String otpSessionId;
    private String email;
    private String otp;
    private Long expireIn;
    private OtpType type;
    private Boolean isExpired = false;
    private Boolean isUsed = false;

}
