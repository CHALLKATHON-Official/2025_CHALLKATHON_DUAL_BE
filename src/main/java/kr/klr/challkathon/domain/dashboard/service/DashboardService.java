package kr.klr.challkathon.domain.dashboard.service;

import kr.klr.challkathon.domain.dashboard.dto.DashboardRes;
import kr.klr.challkathon.domain.exercise.entity.ExerciseCategory;
import kr.klr.challkathon.domain.exercise.entity.ExerciseRecord;
import kr.klr.challkathon.domain.exercise.repository.ExerciseRecordRepository;
import kr.klr.challkathon.domain.exercise.repository.ExerciseRepository;
import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {
    
    private final ExerciseRecordRepository exerciseRecordRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserService userService;
    
    /**
     * 환자 메인 대시보드 정보 조회
     */
    public DashboardRes getPatientDashboard(String userUid) {
        User user = userService.findByUid(userUid);
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6); // 7일간 (오늘 포함)
        
        // 오늘 요약 정보
        DashboardRes.TodaySummary todaySummary = getTodaySummary(user, today);
        
        // 주간 진행상황
        DashboardRes.WeeklyProgress weeklyProgress = getWeeklyProgress(user, weekStart, today);
        
        // 최근 활동
        List<DashboardRes.RecentActivity> recentActivities = getRecentActivities(user, today);
        
        return DashboardRes.builder()
                .todaySummary(todaySummary)
                .weeklyProgress(weeklyProgress)
                .recentActivities(recentActivities)
                .build();
    }
    
    private DashboardRes.TodaySummary getTodaySummary(User user, LocalDate today) {
        List<ExerciseRecord> todayRecords = exerciseRecordRepository
                .findByUserAndExerciseDateOrderByCreatedAtDesc(user, today);
        
        Integer totalSteps = todayRecords.stream()
                .mapToInt(record -> record.getSteps() != null ? record.getSteps() : 0)
                .sum();
        
        Integer totalMinutes = todayRecords.stream()
                .mapToInt(ExerciseRecord::getDurationMinutes)
                .sum();
        
        Integer totalCalories = todayRecords.stream()
                .mapToInt(record -> record.getCaloriesBurned() != null ? record.getCaloriesBurned().intValue() : 0)
                .sum();
        
        // 필수 운동 완료율 계산 (통계 대시보드용)
        long completedRequiredExercises = todayRecords.stream()
                .filter(record -> record.getExercise().getCategory() == ExerciseCategory.REQUIRED)
                .map(record -> record.getExercise().getId())
                .distinct()
                .count();
        
        long totalRequiredExercises = exerciseRepository
                .findByTypeAndCategoryAndIsActiveTrueOrderByOrderIndexAsc(
                        kr.klr.challkathon.domain.exercise.entity.ExerciseType.INDOOR, 
                        ExerciseCategory.REQUIRED)
                .size();
        
        double completionRate = totalRequiredExercises > 0 ? 
                (double) completedRequiredExercises / totalRequiredExercises * 100 : 0.0;
        
        return DashboardRes.TodaySummary.builder()
                .totalSteps(totalSteps)
                .totalMinutes(totalMinutes)
                .totalCalories(totalCalories)
                .completedRequiredExercises((int) completedRequiredExercises)
                .totalRequiredExercises((int) totalRequiredExercises)
                .completionRate(Math.round(completionRate * 10) / 10.0)
                .build();
    }
    
    private DashboardRes.WeeklyProgress getWeeklyProgress(User user, LocalDate startDate, LocalDate endDate) {
        List<ExerciseRecord> weeklyRecords = exerciseRecordRepository
                .findByUserAndExerciseDateBetweenOrderByExerciseDateDesc(user, startDate, endDate);
        
        List<DashboardRes.DailyProgress> dailyProgress = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> {
                    LocalDate date = startDate.plusDays(i);
                    
                    List<ExerciseRecord> dayRecords = weeklyRecords.stream()
                            .filter(record -> record.getExerciseDate().equals(date))
                            .collect(Collectors.toList());
                    
                    Integer steps = dayRecords.stream()
                            .mapToInt(record -> record.getSteps() != null ? record.getSteps() : 0)
                            .sum();
                    
                    Integer minutes = dayRecords.stream()
                            .mapToInt(ExerciseRecord::getDurationMinutes)
                            .sum();
                    
                    Double calories = dayRecords.stream()
                            .mapToDouble(record -> record.getCaloriesBurned() != null ? record.getCaloriesBurned() : 0.0)
                            .sum();
                    
                    // 필수 운동만 카운트 (통계용)
                    Integer completedExercises = (int) dayRecords.stream()
                            .filter(record -> record.getExercise().getCategory() == ExerciseCategory.REQUIRED)
                            .map(record -> record.getExercise().getId())
                            .distinct()
                            .count();
                    
                    String dayOfWeek = date.format(DateTimeFormatter.ofPattern("E", Locale.KOREAN));
                    
                    return DashboardRes.DailyProgress.builder()
                            .date(date)
                            .dayOfWeek(dayOfWeek)
                            .steps(steps)
                            .minutes(minutes)
                            .calories(Math.round(calories * 10) / 10.0)
                            .completedExercises(completedExercises)
                            .build();
                })
                .collect(Collectors.toList());
        
        Integer totalWeeklySteps = dailyProgress.stream()
                .mapToInt(DashboardRes.DailyProgress::getSteps)
                .sum();
        
        return DashboardRes.WeeklyProgress.builder()
                .totalWeeklySteps(totalWeeklySteps)
                .dailyProgress(dailyProgress)
                .build();
    }
    
    private List<DashboardRes.RecentActivity> getRecentActivities(User user, LocalDate today) {
        List<ExerciseRecord> recentRecords = exerciseRecordRepository
                .findByUserAndExerciseDateOrderByCreatedAtDesc(user, today)
                .stream()
                .limit(5)
                .collect(Collectors.toList());
        
        return recentRecords.stream()
                .map(record -> {
                    long hoursAgo = ChronoUnit.HOURS.between(record.getCreatedAt(), 
                            java.time.LocalDateTime.now());
                    
                    String timeAgo;
                    if (hoursAgo < 1) {
                        long minutesAgo = ChronoUnit.MINUTES.between(record.getCreatedAt(), 
                                java.time.LocalDateTime.now());
                        timeAgo = minutesAgo + "분 전";
                    } else if (hoursAgo < 24) {
                        timeAgo = hoursAgo + "시간 전";
                    } else {
                        timeAgo = "오늘";
                    }
                    
                    return DashboardRes.RecentActivity.builder()
                            .exerciseName(record.getExercise().getName())
                            .durationMinutes(record.getDurationMinutes())
                            .timeAgo(timeAgo)
                            .status("완료")
                            .build();
                })
                .collect(Collectors.toList());
    }
}
