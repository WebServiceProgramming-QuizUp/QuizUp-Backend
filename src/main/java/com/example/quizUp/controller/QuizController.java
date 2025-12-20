// src/main/java/com/example/quizUp/controller/QuizController

package com.example.quizUp.controller;

import com.example.quizUp.dto.QuizAbandonRequestDto;
import com.example.quizUp.dto.QuizStartResponseDto;
import com.example.quizUp.dto.QuizSubmissionRequestDto;
import com.example.quizUp.dto.QuizSubmissionResponseDto;
import com.example.quizUp.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    // 1. 퀴즈 풀이 시작 기능 (스테이지 ID를 Input으로 받음)
    // URL 예시: GET /api/quiz/stages/1-1/start
    @GetMapping("/stages/{stageId}/start")
    public ResponseEntity<QuizStartResponseDto> startQuiz(@PathVariable String stageId) {

        // Service 계층에 비즈니스 로직 처리 위임(time_limit, List<quiz_id, question_text> 가져오기)
        QuizStartResponseDto response = quizService.startQuiz(stageId);

        // HTTP 200 OK 응답과 함께 결과 반환
        return ResponseEntity.ok(response);
    }


    // 2. 답안 제출 및 채점 기능
    // 예 POST /api/quiz/submit
    @PostMapping("/submit")
    public ResponseEntity<QuizSubmissionResponseDto> submitQuiz(@RequestBody QuizSubmissionRequestDto request) {
        // [Input: user_id, stage_id, List<quiz_id, user_input>]
        // [Output: history_id, stars, is_cleared]
        QuizSubmissionResponseDto response = quizService.scoreAndSave(request);
        return ResponseEntity.ok(response);
    }

    // 3. 퀴즈 풀이 중단 기능(back 버튼)
    @PostMapping("/abandon")
    public ResponseEntity<String> abandonQuiz(@RequestBody QuizAbandonRequestDto request) {
        // [Input: user_id, stage_id]
        quizService.abandonQuiz(request.getUserId(), request.getStageId());

        return ResponseEntity.ok("퀴즈 포기 처리가 완료되었습니다. 점수에 변화가 없습니다.");
    }

    // 4. 타임 아웃 시
    @PostMapping("/timeout")
    public ResponseEntity<QuizSubmissionResponseDto> timeoutQuiz(@RequestBody QuizSubmissionRequestDto request) {
        // 제한 시간 종료 시에도 기존 채점 로직 그대로 사용
        QuizSubmissionResponseDto response = quizService.scoreAndSave(request);
        return ResponseEntity.ok(response);
    }
}
