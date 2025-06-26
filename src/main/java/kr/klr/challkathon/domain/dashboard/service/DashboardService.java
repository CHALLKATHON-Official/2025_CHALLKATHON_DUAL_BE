package kr.klr.challkathon.domain.dashboard.service;

import kr.klr.challkathon.domain.dashboard.dto.response.PatientDashboardRes;
import kr.klr.challkathon.domain.dashboard.entity.DailyStats;
import kr.klr.challkathon.domain.dashboard.repository.DailyStatsRepository;
import kr.klr.challkathon.domain.exercise.entity.Exercise;
import kr.klr.challkathon.domain.exercise.entity.ExerciseRecord;
import kr.klr.challkathon.domain.exercise.repository.ExerciseRecordRepository;
import kr.klr.challkathon.domain.exercise.repository.ExerciseRepository;
import kr.klr.challkathon.domain.health.entity.HealthRecord;
import kr.klr.challkathon.domain.health.repository.HealthRecordRepository;
import kr.klr.challkathon.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final DailyStatsRepository dailyStatsRepository;
    private final ExerciseRecordRepository exerciseRecordRepository;
    private final ExerciseRepository exerciseRepository;
    private final HealthRecordRepository healthRecordRepository;

    public PatientDashboardRes getPatientDashboard(User user) {
        LocalDate today = LocalDate.now();
        
        // 오늘의 요약 데이터
        PatientDashboardRes.TodaySummary todaySummary = getTodaySummary(user, today);
        
        // 주간 걸음 수 데이터
        PatientDashboardRes.WeeklySteps weeklySteps = getWeeklySteps(user, today);
        
        return PatientDashboardRes.builder()
                .todaySummary(todaySummary)
                .weeklySteps(weeklySteps)
                .build();
    }

    private PatientDashboardRes.TodaySummary getTodaySummary(User user, LocalDate today) {
        // 오늘의 통계 조회 또는 생성
        DailyStats todayStats = dailyStatsRepository.findByUserAndStatsDate(user, today)
                .orElseGet(() -> createEmptyDailyStats(user, today));
        
        // 오늘의 최신 통증 수준 조회
        Integer todayPainLevel = healthRecordRepository.findTopByUserAndRecordDateOrderByRecordTimeDesc(user, today)
                .map(HealthRecord::getTotalPainScore)
                .orElse(0);

        return PatientDashboardRes.TodaySummary.builder()
                .name(user.getNickname() != null ? user.getNickname() : user.getUsername())
                .steps(todayStats.getTotalSteps())
                .exerciseMinutes(todayStats.getTotalExerciseMinutes())
                .distanceKm(todayStats.getTotalDistanceKm())
                .todayPainLevel(todayPainLevel)
                .build();
    }

    private PatientDashboardRes.WeeklySteps getWeeklySteps(User user, LocalDate today) {
        // 이번 주 월요일부터 일요일까지
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);
        
        List<DailyStats> weeklyStats = dailyStatsRepository.findByUserAndStatsDateBetween(user, monday, sunday);
        
        List<PatientDashboardRes.DailyStepData> dailySteps = new ArrayList<>();
        int totalSteps = 0;
        
        for (int i = 0; i < 7; i++) {
            LocalDate date = monday.plusDays(i);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            
            Optional<DailyStats> statsOpt = weeklyStats.stream()
                    .filter(stats -> stats.getStatsDate().equals(date))
                    .findFirst();
            
            int steps = statsOpt.map(DailyStats::getTotalSteps).orElse(0);
            totalSteps += steps;
            
            dailySteps.add(PatientDashboardRes.DailyStepData.builder()
                    .dayOfWeek(dayOfWeek.name())
                    .dayName(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN))
                    .steps(steps)
                    .build());
        }
        
        return PatientDashboardRes.WeeklySteps.builder()
                .dailySteps(dailySteps)
                .totalSteps(totalSteps)
                .build();
    }

    @Transactional
    public void updateDailyStats(User user, LocalDate date) {
        DailyStats dailyStats = dailyStatsRepository.findByUserAndStatsDate(user, date)
                .orElseGet(() -> createEmptyDailyStats(user, date));
        
        // 해당 날짜의 운동 기록들로부터 통계 계산
        List<ExerciseRecord> dayRecords = exerciseRecordRepository.findByUserAndExerciseDate(user, date);
        
        int totalSteps = dayRecords.stream()
                .mapToInt(record -> record.getSteps() != null ? record.getSteps() : 0)
                .sum();
        
        int totalMinutes = dayRecords.stream()
                .mapToInt(ExerciseRecord::getDurationMinutes)
                .sum();
        
        double totalDistance = dayRecords.stream()
                .mapToDouble(record -> record.getDistanceKm() != null ? record.getDistanceKm() : 0.0)
                .sum();
        
        // 필수 운동 완료 개수 계산
        List<Exercise> requiredExercises = exerciseRepository.findByIsRequiredTrueAndIsActiveTrue();
        long completedRequired = dayRecords.stream()
                .filter(record -> requiredExercises.stream()
                        .anyMatch(exercise -> exercise.getId().equals(record.getExercise().getId())))
                .count();
        
        dailyStats.setTotalSteps(totalSteps);
        dailyStats.setTotalExerciseMinutes(totalMinutes);
        dailyStats.setTotalDistanceKm(totalDistance);
        dailyStats.setRequiredExercisesCompleted((int) completedRequired);
        dailyStats.setTotalExercisesCompleted(dayRecords.size());
        
        // 오늘의 통증 수준 업데이트
        if (date.equals(LocalDate.now())) {
            Integer latestPainLevel = healthRecordRepository.findTopByUserAndRecordDateOrderByRecordTimeDesc(user, date)
                    .map(HealthRecord::getTotalPainScore)
                    .orElse(null);
            dailyStats.setTodayPainLevel(latestPainLevel);
        }
        
        dailyStatsRepository.save(dailyStats);
    }

    private DailyStats createEmptyDailyStats(User user, LocalDate date) {
        return DailyStats.builder()
                .user(user)
                .statsDate(date)
                .build();
    }
}
