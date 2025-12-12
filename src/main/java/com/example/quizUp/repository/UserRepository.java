package com.example.quizUp.repository;

import com.example.quizUp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    // 중복 가입 방지를 위해 ID 존재 여부 확인
    boolean existsByUserId(String userId);
}