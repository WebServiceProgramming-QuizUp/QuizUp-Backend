// src/main/java/com/example/quizUp/repository/QuizRepository

package com.example.quizUp.repository;

import com.example.quizUp.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, String>{
    // 특정 Stage ID에 해당하는 모든 퀴즈 목록을 조회하는 메서드
    List<Quiz> findByStageId(String stageId);
}
