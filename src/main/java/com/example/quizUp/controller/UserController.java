package com.example.quizUp.controller;

import com.example.quizUp.dto.MyPageResponseDto;
import com.example.quizUp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 마이페이지 조회
    @GetMapping("/mypage")
    public ResponseEntity<MyPageResponseDto> getMyPage(
            // 토큰에서 유저 ID를 자동으로 꺼내옵니다 (나중에 Security 설정 보완 필요)
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 지금은 @AuthenticationPrincipal이 동작하려면 SecurityConfig 설정이 더 필요하므로
        // 테스트를 위해 임시로 ID를 직접 받거나, 헤더 파싱 로직이 필요합니다.
        // 우선은 "test1"이라고 가정하고 테스트 코드를 짜거나,
        // JWT 필터가 구현되어 있어야 userDetails가 채워집니다.

        // ★ 중요: 현재 JWT 필터가 완전히 적용되지 않았으므로,
        // 테스트를 위해 일단 하드코딩된 ID로 로직이 도는지 확인하겠습니다.
        String userId = "test1";

        MyPageResponseDto result = userService.getMyPageInfo(userId);
        return ResponseEntity.ok(result);
    }
}