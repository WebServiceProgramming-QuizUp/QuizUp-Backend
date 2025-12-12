package com.example.quizUp.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users") // DB 예약어 user와 겹칠 수 있어 users로 명명 추천
public class User {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId; // 사용자가 입력하는 ID (UI의 'id' 필드)

    @Column(nullable = false)
    private String password; // 암호화되어 저장될 비밀번호

    @Column(nullable = false)
    private String username; // 닉네임 (UI의 'username')

    @Column(name = "total_stars")
    private int totalStars = 0; // 기본값 0

    @Enumerated(EnumType.STRING)
    private Tier tier;

    // Tier 변경을 위한 편의 메서드
    public void updateTier(Tier tier) {
        this.tier = tier;
    }

    @Builder
    public User(String userId, String password, String username, int totalStars, Tier tier) {
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.totalStars = totalStars;
        this.tier = (tier != null) ? tier : Tier.T1;
    }

}