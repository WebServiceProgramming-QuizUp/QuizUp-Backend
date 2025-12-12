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

    // Tier와의 관계는 나중에 Tier 엔티티 만들고 연결하겠습니다.
    // 우선은 회원가입/로그인 흐름부터 뚫기 위해 주석 처리하거나, 단순 ID만 저장해도 됩니다.
    // @ManyToOne
    // @JoinColumn(name = "tier_id")
    // private Tier tier;

    @Builder
    public User(String userId, String password, String username, int totalStars) {
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.totalStars = totalStars;
    }
}