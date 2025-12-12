package com.example.quizUp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyPageResponseDto {
    private String username;
    private String tierName;    // 현재 등급 (ex: 대학생)
    private int totalStars;     // 현재 모은 별
    private int progress;
}
