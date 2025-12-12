package com.example.quizUp.service;

import com.example.quizUp.domain.User;
import com.example.quizUp.dto.SignupRequestDto;
import com.example.quizUp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String signup(SignupRequestDto requestDto) {
        // 1. 중복 ID 검사
        if (userRepository.existsByUserId(requestDto.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 3. 유저 객체 생성 (빌더 패턴 사용)
        User user = User.builder()
                .userId(requestDto.getUserId())
                .password(encodedPassword)
                .username(requestDto.getUsername())
                .totalStars(0) // 초기값
                .build();

        // 4. DB 저장
        userRepository.save(user);

        return "회원가입 성공: " + user.getUsername();
    }
}