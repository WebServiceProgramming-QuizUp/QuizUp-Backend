// src/main/java/com/example/quizUp/domain/UserHistory

package com.example.quizUp.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_history")
public class UserHistory {

    @Id
    @Column(name = "history_id")
    private String historyId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "stage_id", nullable = false)
    private String stageId;

    @Column(name = "tier_id")
    private String tierId; // 채점 당시의 티어 저장

    /*
    @Column(name = "world_id")
    private String worldId;
     */

    private int stars = 0; // 획득한 별 개수

    @Column(name = "is_cleared")
    private boolean isCleared = false;

    @Column(name = "submit_log", columnDefinition = "TEXT") // JSON 형식의 로그 데이터
    private String submitLog;

    @Builder
    public UserHistory(String userId, String stageId, String tierId, int stars, boolean isCleared, String submitLog) {
        this.userId = userId;
        this.stageId = stageId;
        this.tierId = tierId;
        this.stars = stars;
        this.isCleared = isCleared;
        this.submitLog = submitLog;
    }

    public void updateRecord(int newStars, boolean isCleared, String newSubmitLog, String currentTier) {
        // 더 높은 별을 얻었다면 갱신
        if (newStars > this.stars) {
            this.stars = newStars;
        }
        this.isCleared = isCleared || this.isCleared; // 한 번이라도 클리어했다면 true 유지
        this.submitLog = newSubmitLog;
        this.tierId = currentTier;
    }
}