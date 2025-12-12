package com.example.quizUp.controller;

import com.example.quizUp.dto.JwtTokenDto;
import com.example.quizUp.dto.LoginRequestDto;
import com.example.quizUp.dto.SignupRequestDto;
import com.example.quizUp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto requestDto) {
        String result = authService.signup(requestDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> login(@RequestBody LoginRequestDto requestDto) {
        JwtTokenDto tokenDto = authService.login(requestDto);
        return ResponseEntity.ok(tokenDto);
    }

}