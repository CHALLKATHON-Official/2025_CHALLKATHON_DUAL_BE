package kr.klr.challkathon.domain.exercise.service;

import kr.klr.challkathon.domain.dashboard.service.DashboardService;
import kr.klr.challkathon.domain.exercise.dto.request.SimpleExerciseRecordReq;
import kr.klr.challkathon.domain.exercise.dto.request.WalkingExerciseRecordReq;
import kr.klr.challkathon.domain.exercise.dto.response.*;
import kr.klr.challkathon.domain.exercise.entity.Exercise;
import kr.klr.challkathon.domain.exercise.entity.ExerciseRecord;
import kr.klr.challkathon.domain.exercise.entity.ExerciseType;
import kr.klr.challkathon.domain.exercise.repository.ExerciseRecordRepository;
import kr.klr.challkathon.domain.exercise.repository.ExerciseRepository;
import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.global.globalResponse.error.ErrorCode;
import kr.klr.challkathon.global.globalResponse.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseRecordRepository exerciseRecordRepository;
    private final DashboardService dashboardService;

    public IndoorExerciseStatusRes getIndoorExerciseStatus(User user) {
        LocalDate today = LocalDate.now();
        
        // 오늘의 진행 상황
        IndoorExerciseStatusRes.TodayProgress todayProgress = getTodayProgress(user, today);
        
        // 필수 운동 목록
        List<Exercise> requiredExercises = exerciseRepository.findByIsRequiredTrueAndIsActiveTrue();
        List<IndoorExerciseStatusRes.ExerciseItem> requiredItems = createExerciseItems(requiredExercises, user, today, true);
        
        // 추천 운동 목록
        List<Exercise> recommendedExercises = exerciseRepository.findByIsRequiredFalseAndIsActiveTrue();
        List<IndoorExerciseStatusRes.ExerciseItem> recommendedItems = createExerciseItems(recommendedExercises, user, today, false);
        
        return IndoorExerciseStatusRes.builder()
                .todayProgress(todayProgress)
                .requiredExercises(requiredItems)
                .recommendedExercises(recommendedItems)
                .build();
    }

    public OutdoorExerciseStatusRes getOutdoorExerciseStatus(User user) {
        // 최고 거리 기록
        Double maxDistance = exerciseRecordRepository.getMaxDistanceByUser(user).orElse(0.0);
        
        // 전날 기록
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<ExerciseRecord> yesterdayRecords = exerciseRecordRepository.findByUserAndExerciseDate(user, yesterday);
        
        OutdoorExerciseStatusRes.YesterdayRecord yesterdayRecord = OutdoorExerciseStatusRes.YesterdayRecord.builder()
                .durationMinutes(yesterdayRecords.stream().mapToInt(ExerciseRecord::getDurationMinutes).sum())
                .distanceKm(yesterdayRecords.stream().mapToDouble(r -> r.getDistanceKm() != null ? r.getDistanceKm() : 0.0).sum())
                .build();
        
        return OutdoorExerciseStatusRes.builder()
                .maxDistanceRecord(maxDistance)
                .yesterdayRecord(yesterdayRecord)
                .build();
    }

    @Transactional
    public String recordWalkingExercise(User user, WalkingExerciseRecordReq request) {
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "운동 정보를 찾을 수 없습니다."));
        
        ExerciseRecord record = ExerciseRecord.builder()
                .user(user)
                .exercise(exercise)
                .exerciseDate(LocalDate.now())
                .startTime(LocalDateTime.now().minusMinutes(request.getDurationMinutes()))
                .endTime(LocalDateTime.now())
                .durationMinutes(request.getDurationMinutes())
                .steps(request.getSteps())
                .distanceKm(request.getDistanceKm())
                .caloriesBurned(request.getCaloriesBurned())
                .notes(request.getNotes())
                .build();
        
        // 페이스 계산
        record.calculatePace();
        
        exerciseRecordRepository.save(record);
        
        // 일일 통계 업데이트
        dashboardService.updateDailyStats(user, LocalDate.now());
        
        return "가벼운 걷기 운동이 기록되었습니다.";
    }

    @Transactional
    public String recordSimpleExercise(User user, SimpleExerciseRecordReq request) {
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "운동 정보를 찾을 수 없습니다."));
        
        ExerciseRecord record = ExerciseRecord.builder()
                .user(user)
                .exercise(exercise)
                .exerciseDate(LocalDate.now())
                .startTime(LocalDateTime.now().minusMinutes(request.getDurationMinutes()))
                .endTime(LocalDateTime.now())
                .durationMinutes(request.getDurationMinutes())
                .notes(request.getNotes())
                .build();
        
        exerciseRecordRepository.save(record);
        
        // 일일 통계 업데이트
        dashboardService.updateDailyStats(user, LocalDate.now());
        
        return exercise.getName() + " 운동이 기록되었습니다.";
    }

    public ExerciseHistoryRes getExerciseHistory(User user, ExerciseType exerciseType, LocalDate startDate, LocalDate endDate) {
        List<ExerciseRecord> records;
        
        if (exerciseType == null) {
            // 전체 운동 기록
            records = exerciseRecordRepository.findByUserAndExerciseDateBetweenOrderByExerciseDateDesc(user, startDate, endDate);
        } else {
            // 특정 타입의 운동 기록만
            records = exerciseRecordRepository.findByUserAndExerciseDateBetweenOrderByExerciseDateDesc(user, startDate, endDate)
                    .stream()
                    .filter(record -> record.getExercise().getType() == exerciseType)
                    .collect(Collectors.toList());
        }
        
        // 통계 계산
        ExerciseHistoryRes.ExerciseStatistics statistics = ExerciseHistoryRes.ExerciseStatistics.builder()
                .totalExerciseCount(records.size())
                .totalExerciseMinutes(records.stream().mapToInt(ExerciseRecord::getDurationMinutes).sum())
                .totalSteps(records.stream().mapToInt(r -> r.getSteps() != null ? r.getSteps() : 0).sum())
                .totalDistanceKm(records.stream().mapToDouble(r -> r.getDistanceKm() != null ? r.getDistanceKm() : 0.0).sum())
                .totalCaloriesBurned(records.stream().mapToDouble(r -> r.getCaloriesBurned() != null ? r.getCaloriesBurned() : 0.0).sum())
                .build();
        
        // 운동 기록 목록
        List<ExerciseHistoryRes.ExerciseHistoryItem> historyItems = records.stream()
                .map(record -> ExerciseHistoryRes.ExerciseHistoryItem.builder()
                        .recordId(record.getRecordId())
                        .exerciseType(record.getExercise().getType())
                        .exerciseDate(record.getExerciseDate())
                        .startTime(record.getStartTime())
                        .exerciseName(record.getExercise().getName())
                        .durationMinutes(record.getDurationMinutes())
                        .steps(record.getSteps())
                        .distanceKm(record.getDistanceKm())
                        .caloriesBurned(record.getCaloriesBurned())
                        .build())
                .collect(Collectors.toList());
        
        return ExerciseHistoryRes.builder()
                .statistics(statistics)
                .exerciseRecords(historyItems)
                .build();
    }

    private IndoorExerciseStatusRes.TodayProgress getTodayProgress(User user, LocalDate today) {
        List<ExerciseRecord> todayRecords = exerciseRecordRepository.findByUserAndExerciseDate(user, today);
        
        // 필수 운동 리스트
        List<Exercise> requiredExercises = exerciseRepository.findByIsRequiredTrueAndIsActiveTrue();
        
        // 완료된 필수 운동 개수
        long completedRequired = todayRecords.stream()
                .filter(record -> requiredExercises.stream()
                        .anyMatch(exercise -> exercise.getId().equals(record.getExercise().getId())))
                .count();
        
        // 총 운동 시간
        int totalMinutes = todayRecords.stream().mapToInt(ExerciseRecord::getDurationMinutes).sum();
        
        // 필수 운동 완료율
        double completionRate = requiredExercises.isEmpty() ? 1.0 : (double) completedRequired / requiredExercises.size();
        
        return IndoorExerciseStatusRes.TodayProgress.builder()
                .completedRequiredExercises((int) completedRequired)
                .totalExerciseMinutes(totalMinutes)
                .requiredExerciseCompletionRate(completionRate)
                .build();
    }

    private List<IndoorExerciseStatusRes.ExerciseItem> createExerciseItems(List<Exercise> exercises, User user, LocalDate today, boolean isRequired) {
        List<ExerciseRecord> todayRecords = exerciseRecordRepository.findByUserAndExerciseDate(user, today);
        
        return exercises.stream()
                .map(exercise -> {
                    boolean isCompleted = todayRecords.stream()
                            .anyMatch(record -> record.getExercise().getId().equals(exercise.getId()));
                    
                    return IndoorExerciseStatusRes.ExerciseItem.builder()
                            .exerciseId(exercise.getId())
                            .name(exercise.getName())
                            .description(exercise.getDescription())
                            .isCompleted(isCompleted)
                            .isRequired(isRequired)
                            .orderIndex(exercise.getOrderIndex())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
