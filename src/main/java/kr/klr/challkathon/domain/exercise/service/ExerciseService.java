package kr.klr.challkathon.domain.exercise.service;

import kr.klr.challkathon.domain.exercise.dto.request.ExerciseRecordReq;
import kr.klr.challkathon.domain.exercise.dto.response.ExerciseListRes;
import kr.klr.challkathon.domain.exercise.dto.response.ExerciseRecordRes;
import kr.klr.challkathon.domain.exercise.dto.response.ExerciseRes;
import kr.klr.challkathon.domain.exercise.entity.*;
import kr.klr.challkathon.domain.exercise.repository.ExerciseRecordRepository;
import kr.klr.challkathon.domain.exercise.repository.ExerciseRepository;
import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.domain.user.service.UserService;
import kr.klr.challkathon.global.globalResponse.error.ErrorCode;
import kr.klr.challkathon.global.globalResponse.error.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseService {
    
    private final ExerciseRepository exerciseRepository;
    private final ExerciseRecordRepository exerciseRecordRepository;
    private final UserService userService;
    
    /**
     * 실내 운동 목록 조회 (완료 여부 포함)
     */
    public ExerciseListRes getIndoorExercises(String userUid) {
        User user = userService.findByUid(userUid);
        LocalDate today = LocalDate.now();
        
        // 오늘 완료한 운동 ID 조회
        Set<Long> completedExerciseIds = exerciseRecordRepository
                .findByUserAndExerciseDateOrderByCreatedAtDesc(user, today)
                .stream()
                .map(record -> record.getExercise().getId())
                .collect(Collectors.toSet());
        
        // 모든 실내 운동 조회
        List<Exercise> allIndoorExercises = exerciseRepository
                .findActiveExercisesByTypeGroupedByCategory(ExerciseType.INDOOR);
        
        // 필수 운동과 추천 운동으로 분리
        List<ExerciseRes> requiredExercises = allIndoorExercises.stream()
                .filter(exercise -> exercise.getCategory() == ExerciseCategory.REQUIRED)
                .map(exercise -> ExerciseRes.fromEntity(exercise, completedExerciseIds.contains(exercise.getId())))
                .collect(Collectors.toList());
        
        List<ExerciseRes> recommendedExercises = allIndoorExercises.stream()
                .filter(exercise -> exercise.getCategory() == ExerciseCategory.RECOMMENDED)
                .map(exercise -> ExerciseRes.fromEntity(exercise, completedExerciseIds.contains(exercise.getId())))
                .collect(Collectors.toList());
        
        // 요약 정보 생성
        int totalExercises = allIndoorExercises.size();
        int completedExercises = completedExerciseIds.size();
        int totalRequiredExercises = requiredExercises.size();
        int completedRequiredExercises = (int) requiredExercises.stream()
                .mapToLong(ex -> ex.getIsCompleted() ? 1 : 0)
                .sum();
        
        double completionRate = totalExercises > 0 ? 
                (double) completedExercises / totalExercises * 100 : 0.0;
        
        ExerciseListRes.ExerciseSummary summary = ExerciseListRes.ExerciseSummary.builder()
                .totalExercises(totalExercises)
                .completedExercises(completedExercises)
                .totalRequiredExercises(totalRequiredExercises)
                .completedRequiredExercises(completedRequiredExercises)
                .completionRate(Math.round(completionRate * 10) / 10.0) // 소수점 1자리
                .build();
        
        return ExerciseListRes.builder()
                .requiredExercises(requiredExercises)
                .recommendedExercises(recommendedExercises)
                .summary(summary)
                .build();
    }
    
    /**
     * 운동 기록 저장
     */
    @Transactional
    public ExerciseRecordRes recordExercise(String userUid, ExerciseRecordReq request) {
        User user = userService.findByUid(userUid);
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new GlobalException(ErrorCode.EXERCISE_NOT_FOUND));
        
        LocalDate today = LocalDate.now();
        
        // 오늘 이미 완료한 운동인지 확인
        exerciseRecordRepository.findByUserAndDateAndExercise(user, today, exercise.getId())
                .ifPresent(existingRecord -> {
                    throw new GlobalException(ErrorCode.EXERCISE_ALREADY_COMPLETED);
                });
        
        // 칼로리 계산
        Double caloriesBurned = exercise.getCaloriesPerMinute() != null ? 
                exercise.getCaloriesPerMinute() * request.getDurationMinutes() : null;
        
        // 운동 기록 저장
        ExerciseRecord record = ExerciseRecord.builder()
                .user(user)
                .exercise(exercise)
                .exerciseDate(today)
                .durationMinutes(request.getDurationMinutes())
                .caloriesBurned(caloriesBurned)
                .steps(request.getSteps())
                .distanceKm(request.getDistanceKm())
                .completionStatus(CompletionStatus.COMPLETED)
                .notes(request.getNotes())
                .build();
        
        ExerciseRecord savedRecord = exerciseRecordRepository.save(record);
        
        log.info("운동 기록 저장 완료: user={}, exercise={}, duration={}분", 
                userUid, exercise.getName(), request.getDurationMinutes());
        
        return ExerciseRecordRes.fromEntity(savedRecord);
    }
    
    /**
     * 사용자의 오늘 운동 기록 조회
     */
    public List<ExerciseRecordRes> getTodayExerciseRecords(String userUid) {
        User user = userService.findByUid(userUid);
        LocalDate today = LocalDate.now();
        
        return exerciseRecordRepository.findByUserAndExerciseDateOrderByCreatedAtDesc(user, today)
                .stream()
                .map(ExerciseRecordRes::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 기간 운동 기록 조회
     */
    public List<ExerciseRecordRes> getExerciseRecords(String userUid, LocalDate startDate, LocalDate endDate) {
        User user = userService.findByUid(userUid);
        
        return exerciseRecordRepository.findByUserAndExerciseDateBetweenOrderByExerciseDateDesc(
                user, startDate, endDate)
                .stream()
                .map(ExerciseRecordRes::fromEntity)
                .collect(Collectors.toList());
    }
}
