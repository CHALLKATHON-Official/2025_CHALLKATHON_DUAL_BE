package kr.klr.challkathon.domain.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardRes {
    
    private TodaySummary todaySummary;
    private WeeklyProgress weeklyProgress;
    private List<RecentActivity> recentActivities;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TodaySummary {
        private Integer totalSteps;
        private Integer totalMinutes;
        private Integer totalCalories;
        private Integer completedRequiredExercises;
        private Integer totalRequiredExercises;
        private Double completionRate;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklyProgress {
        private Integer totalWeeklySteps;
        private List<DailyProgress> dailyProgress;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyProgress {
        private LocalDate date;
        private String dayOfWeek;
        private Integer steps;
        private Integer minutes;
        private Double calories;
        private Integer completedExercises;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentActivity {
        private String exerciseName;
        private Integer durationMinutes;
        private String timeAgo;
        private String status;
    }
}
