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
}