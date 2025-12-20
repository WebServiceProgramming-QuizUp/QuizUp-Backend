// src/main/java/com/example/quizUp/dto/QuizSubmissionRequestDto
// 채점 위해 사용자작성답안 제출하는 요청 dto

package com.example.quizUp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class QuizSubmissionRequestDto {
    private String userId;
    private String stageId;
    private List<QuizInputDto> quizInputs;

    @Getter
    @NoArgsConstructor
    public static class QuizInputDto {
        private String quizId;
        private String userInput;
    }
}