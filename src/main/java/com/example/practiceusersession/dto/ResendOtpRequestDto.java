package com.example.practiceusersession.dto;

import com.example.practiceusersession.model.enums.OtpType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResendOtpRequestDto {
    String registeredEmail;
    OtpType type;
}
