package kr.klr.challkathon.domain.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDashboardRes {
    
    private TodaySummary todaySummary;
    private WeeklySteps weeklySteps;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TodaySummary {
        private String name;
        private Integer steps;
        private Integer exerciseMinutes;
        private Double distanceKm;
        private Integer todayPainLevel; // 0-15점
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklySteps {
        private List<DailyStepData> dailySteps; // 월~일
        private Integer totalSteps;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyStepData {
        private String dayOfWeek; // MON, TUE, WED, THU, FRI, SAT, SUN
        private String dayName; // 월, 화, 수, 목, 금, 토, 일
        private Integer steps;
    }
}
