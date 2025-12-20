// src/main/java/com/example/quizUp/repository/UserHistoryRepository

package com.example.quizUp.repository;

import com.example.quizUp.domain.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    // 특정 유저가 특정 스테이지를 푼 기록을 찾기
    Optional<UserHistory> findByUserIdAndStageId(String userId, String stageId);
}