package com.example.quizUp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users") // DB 예약어 user와 겹칠 수 있어 users로 명명 추천
public class User implements UserDetails {

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

    public void addStars(int starsToAdd) {
        this.totalStars += starsToAdd;
    }

    // UserDetails 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 기본 권한 "ROLE_USER"를 부여.
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.userId; // Spring Security에서는 username을 ID로 사용합니다.
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부 (true: 만료되지 않음)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부 (true: 잠기지 않음)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부 (true: 만료되지 않음)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부 (true: 활성화됨)
    }
}
