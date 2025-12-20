// src/main/java/com/example/quizUp/domain/Quiz

package com.example.quizUp.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "quiz")
public class Quiz {

    @Id
    @Column(name = "quiz_id", nullable = false, unique = true)
    private String quizId;

    @Column(name = "stage_id", nullable = false)
    private String stageId;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    //world도 있는 개념인지 모르겠어서 일단 주석
    /*
    @Column(name = "correct_answer", nullable = false)
    private String worldId;
    */


    // @Builder를 사용하여 안전하게 객체 생성
    @Builder
    public Quiz(String quizId, String stageId, String questionText, String correctAnswer, String quizType) {
        this.quizId = quizId;
        this.stageId = stageId;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }
}
