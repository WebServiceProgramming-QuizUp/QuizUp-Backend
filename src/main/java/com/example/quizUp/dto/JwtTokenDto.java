package com.example.quizUp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtTokenDto {
    private String accessToken;
    private String grantType; // typically "Bearer"
}
