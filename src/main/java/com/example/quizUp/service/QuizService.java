// src/main/java/com/example/quizUp/service/QuizService

package com.example.quizUp.service;

import com.example.quizUp.domain.Quiz;
import com.example.quizUp.domain.Stage;
import com.example.quizUp.domain.User;
import com.example.quizUp.domain.UserHistory;
import com.example.quizUp.dto.QuizStartResponseDto;
import com.example.quizUp.dto.QuizStartResponseDto.QuizItemDto;
import com.example.quizUp.dto.QuizSubmissionRequestDto;
import com.example.quizUp.dto.QuizSubmissionResponseDto;
import com.example.quizUp.repository.QuizRepository;
import com.example.quizUp.repository.StageRepository;
import com.example.quizUp.repository.UserHistoryRepository;
import com.example.quizUp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final StageRepository stageRepository;
    private final UserHistoryRepository userHistoryRepository;
    private final UserRepository userRepository;
    private final UserService userService; // 유저 정보 갱신용

    // ** 퀴즈 풀이 시작 기능
    @Transactional(readOnly = true)
    public QuizStartResponseDto startQuiz(String stageId) {

        // 1. Stage 엔티티 조회 (timeLimit을 가져오기 위함)
        // StageRepository를 사용하여 Stage ID로 조회합니다.
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new RuntimeException("해당 스테이지를 찾을 수 없습니다.")); // 스테이지 없으면 예외 처리

        // 2. Repository를 통해 DB에서 해당 스테이지의 퀴즈 목록 조회
        List<Quiz> quizzes = quizRepository.findByStageId(stageId);

        if (quizzes.isEmpty()) {
            throw new RuntimeException("해당 스테이지의 퀴즈를 찾을 수 없습니다.");
        }

        // 3. 응답 DTO를 생성하고 timeLimit 설정(Stage 엔티티에서 timeLimit 값을 가져온다.)
        QuizStartResponseDto response = new QuizStartResponseDto();
        response.setTimeLimit(stage.getTimeLimit());


        // 4. 퀴즈 목록(정답제외) 불러오고 itemDTO 형태로 변환 후 DTO에 담기
        List<QuizItemDto> quizItemDtos = quizzes.stream()
                .map(quiz -> new QuizItemDto(quiz.getQuizId(), quiz.getQuestionText()))
                .collect(Collectors.toList());

        response.setQuizList(quizItemDtos);

        return response;
    }

    // ** 답안 제출 및 채점 기능
    @Transactional
    public QuizSubmissionResponseDto scoreAndSave(QuizSubmissionRequestDto request) {
        // 1. 유저 정보 조회 (티어 정보 확인용)
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 2. 해당 스테이지의 정답 데이터 Map으로 변환
        List<Quiz> quizzes = quizRepository.findByStageId(request.getStageId());
        Map<String, String> answerMap = quizzes.stream()
                .collect(Collectors.toMap(Quiz::getQuizId, Quiz::getCorrectAnswer));

        // 3. 채점 진행 및 상세 결과(Details) 생성
        List<QuizSubmissionResponseDto.QuizDetailDto> details = new java.util.ArrayList<>(); // 상세 결과 리스트
        int correctCount = 0;

        for (QuizSubmissionRequestDto.QuizInputDto input : request.getQuizInputs()) {
            String correctAnswer = answerMap.get(input.getQuizId());
            boolean isCorrect = correctAnswer != null && correctAnswer.equals(input.getUserInput()); // 정답 여부 판별

            if (isCorrect) {
                correctCount++;
            }

            // 각 문제의 결과를 DTO에 담아 리스트에 추가
            details.add(new QuizSubmissionResponseDto.QuizDetailDto(
                    input.getQuizId(),
                    input.getUserInput(),
                    correctAnswer,
                    isCorrect
            ));
        }

        // 4. 별 개수 및 클리어 여부 계산
        int newStars = calculateStars(correctCount);
        boolean isCleared = correctCount >= 5;

        // 5. 기존 기록 조회 및 갱신 (특정 유저 특정 스테이지당 userHistory 1개만 유지)
        Optional<UserHistory> optionalHistory = userHistoryRepository
                .findByUserIdAndStageId(request.getUserId(), request.getStageId());

        int deltaStars = 0; // 증가할 별 개수(유저 정보 갱신 위해)
        UserHistory history;

        if (optionalHistory.isPresent()) {
            // [케이스 A] 기존 기록이 있는 경우: 업데이트 로직
            history = optionalHistory.get();
            int previousBest = history.getStars();

            // 더 높은 별점을 획득했을 때만 증가분 계산
            if (newStars > previousBest) {
                deltaStars = newStars - previousBest;
            }

            // 엔티티 내부의 비즈니스 메서드로 값 갱신 (Dirty Checking 활용)
            history.updateRecord(newStars, isCleared, request.getQuizInputs().toString(), user.getTier().name());
        } else {
            // [케이스 B] 처음 푸는 경우: 신규 생성 로직
            deltaStars = newStars;
            history = UserHistory.builder()
                    .userId(request.getUserId())
                    .stageId(request.getStageId())
                    .tierId(user.getTier().name())
                    .stars(newStars)
                    .isCleared(isCleared)
                    .submitLog(request.getQuizInputs().toString())
                    .build();

            userHistoryRepository.save(history);
        }


        // 6. 유저의 총 별 개수 및 티어 갱신 (증가분 deltaStars가 있을 때만!)
        if (deltaStars > 0) {
            userService.increaseUserStars(user.getUserId(), deltaStars);
        }

        // 최종 반환 시 상세 결과 리스트(details)를 함께 전달합니다.
        return new QuizSubmissionResponseDto(
                history.getHistoryId(),
                newStars,
                isCleared,
                correctCount,
                details
        );

    }

    // * 맞은 개수에 따라 별 계산
    private int calculateStars(int correctCount) {
        if (correctCount == 10) return 3;
        if (10 > correctCount && correctCount >= 6) return 2;
        if (correctCount == 5) return 1;
        return 0;
    }


    // ** 퀴즈 풀이 중단 기능(back 버튼)
    @Transactional
    public void abandonQuiz(String userId, String stageId) {
        // 동작 없이 종료
    }
}


