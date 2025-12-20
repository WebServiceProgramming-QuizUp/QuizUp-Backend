package com.example.quizUp.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 헤더에서 Authorization 값을 꺼냄
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userId = null;

        // 2. "Bearer "로 시작하는지 확인하고 토큰만 잘라냄
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            // 3. 토큰이 유효한지 검사하고 ID를 꺼냄
            if (jwtTokenProvider.validateToken(token)) {
                userId = jwtTokenProvider.getUserId(token);
            }
        }

        // 4. ID가 있고, 아직 인증되지 않은 상태라면 인증 처리 진행
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

            // "이 사람은 인증된 사용자입니다"라는 Token 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 시큐리티 컨텍스트(저장소)에 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 5. 다음 단계로 통과
        filterChain.doFilter(request, response);
    }
}