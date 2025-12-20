// src/main/java/com/example/quizUp/repository/StageRepository

package com.example.quizUp.repository;

import com.example.quizUp.domain.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageRepository extends JpaRepository<Stage, String> {
    // Stage ID로 Stage 엔티티를 찾는 기본 메서드 findById(String id) 제공됨.
}
