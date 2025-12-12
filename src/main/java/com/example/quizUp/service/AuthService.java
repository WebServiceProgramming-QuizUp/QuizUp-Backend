package com.example.quizUp.service;

import com.example.quizUp.domain.User;
import com.example.quizUp.dto.JwtTokenDto;
import com.example.quizUp.dto.LoginRequestDto;
import com.example.quizUp.dto.SignupRequestDto;
import com.example.quizUp.jwt.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;

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

    // 로그인 메서드
    @Transactional(readOnly = true)
    public JwtTokenDto login(LoginRequestDto requestDto) {
        // 1. ID로 유저 찾기
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));

        // 2. 비밀번호 확인 (입력비번, DB비번 비교)
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 3. 토큰 생성
        String token = jwtTokenProvider.createToken(user.getUserId());

        // 4. 토큰 반환
        return new JwtTokenDto(token, "Bearer");
    }
}