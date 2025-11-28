package com.example.practiceusersession.dto;

import com.example.practiceusersession.model.enums.OtpType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDto {
    private UUID tempSessionId;
    private Long expireIn;
    private OtpType type;
}
