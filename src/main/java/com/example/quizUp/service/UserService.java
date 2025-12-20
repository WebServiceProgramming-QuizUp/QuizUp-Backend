package com.example.quizUp.service;

import com.example.quizUp.domain.Tier;
import com.example.quizUp.domain.User;
import com.example.quizUp.dto.MyPageResponseDto;
import com.example.quizUp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public MyPageResponseDto getMyPageInfo(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // Enum에서 바로 정보 획득
        Tier currentTier = user.getTier();
        int nextGoal = currentTier.getNextMinStars();
        int progress = 0;

        // 명세서 공식 적용: (total_stars / next_min_stars) * 100
        // 단, 마지막 단계(교수)라서 목표가 0인 경우 등은 예외 처리
        if (nextGoal > 0) {
            progress = (int) ((double) user.getTotalStars() / nextGoal * 100);
            if (progress > 100) progress = 100;
        } else {
            progress = 100; // 목표치가 0이면(최고레벨) 100% 처리
        }

        return MyPageResponseDto.builder()
                .username(user.getUsername())
                .tierName(currentTier.getName())
                .totalStars(user.getTotalStars())
                .progress(progress)
                .build();
    }

    // * 유저 별 개수 갱신
    @Transactional // @Transactional 덕분에 메서드가 끝나면 DB에 자동으로 UPDATE 쿼리가 날아갑니다.
    public void increaseUserStars(String userId, int deltaStars) { // deltaStars(증가분)를 인자로 받음
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 1. 엔티티 내부 메서드를 통해 안전하게 별 합산 (Setter 대신 사용)
        user.addStars(deltaStars);

        // 2. 합산된 최종 별점을 기준으로 티어 계산
        int updatedTotalStars = user.getTotalStars();

        // Tier Enum을 순회하며 현재 점수에 맞는 티어 설정
        for (Tier tier : Tier.values()) {
            if (updatedTotalStars >= tier.getMinStars() && updatedTotalStars < tier.getNextMinStars()) {
                user.updateTier(tier); // User 엔티티의 편의 메서드 활용
                break;
            }
        }

    }
}