package kr.klr.challkathon.domain.exercise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDetailRes {
    
    private Long exerciseId;
    private String exerciseName;
    private ExerciseResult exerciseResult;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExerciseResult {
        // 가벼운 걷기용 상세 데이터
        private Integer steps;
        private Integer durationMinutes;
        private Double distanceKm;
        private Double caloriesBurned;
        private Double paceMinPerKm;
        
        // 다른 운동용 (시간만)
        private Integer exerciseTimeMinutes;
    }
}
