package kr.klr.challkathon.domain.exercise.dto.response;

import kr.klr.challkathon.domain.exercise.entity.ExerciseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseHistoryRes {
    
    private ExerciseStatistics statistics;
    private List<ExerciseHistoryItem> exerciseRecords;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExerciseStatistics {
        private Integer totalExerciseCount;
        private Integer totalExerciseMinutes;
        private Integer totalSteps;
        private Double totalDistanceKm;
        private Double totalCaloriesBurned;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExerciseHistoryItem {
        private String recordId;
        private ExerciseType exerciseType; // INDOOR, OUTDOOR
        private LocalDate exerciseDate;
        private LocalDateTime startTime;
        private String exerciseName;
        private Integer durationMinutes;
        
        // 걷기 운동 한정 추가 정보
        private Integer steps;
        private Double distanceKm;
        private Double caloriesBurned;
    }
}
