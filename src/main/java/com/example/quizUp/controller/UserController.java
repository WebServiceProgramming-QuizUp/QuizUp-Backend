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
        MyPageResponseDto result = userService.getMyPageInfo(userDetails.getUsername());
        return ResponseEntity.ok(result);
    }
}