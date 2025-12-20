// src/main/java/com/example/quizUp/dto/QuizSubmissionResponseDto
// 채점 후 결과와 틀린 문제 확인 위한 정보 담은 dto

package com.example.quizUp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class QuizSubmissionResponseDto {
    private String historyId;
    private int stars;
    private boolean isCleared;
    private int correctCount; // 맞힌 개수

    private List<QuizDetailDto> details; // 틀린 문제 확인용 데이터

    @Getter
    @AllArgsConstructor
    public static class QuizDetailDto {
        private String quizId;
        private String userInput;
        private String correctAnswer;
        private boolean isCorrect;
    }
}