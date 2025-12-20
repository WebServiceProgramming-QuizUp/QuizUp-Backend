// src/main/java/com/example/quizUp/dto/QuizStartResponseDto
// 해당 스테이지의 문제 및 정보를 담은 퀴즈시작응답 dto

package com.example.quizUp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizStartResponseDto {
    // 요구사항: 해당 스테이지의 time_limit
    private int timeLimit;

    // 요구사항: List<quiz_id, question_text>
    private List<QuizItemDto> quizList;

    // 내부 클래스로 문제 한 개당 정보를 정의
    @Getter
    @Setter
    public static class QuizItemDto {
        private String quizId;      // 문제 ID (DB 엔티티와 일치하도록 String 사용)
        private String questionText; // 문제 내용

        // 생성자
        public QuizItemDto(String quizId, String questionText) {
            this.quizId = quizId;
            this.questionText = questionText;
        }
    }
}