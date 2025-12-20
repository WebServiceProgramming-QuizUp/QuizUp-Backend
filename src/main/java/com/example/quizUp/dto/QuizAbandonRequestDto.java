// src/main/java/com/example/quizUp/dto/QuizAbandonRequestDto
// 퀴즈 중도 포기 시(back 버튼)의 요청 dto

package com.example.quizUp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuizAbandonRequestDto {
    private String userId;
    private String stageId;
}