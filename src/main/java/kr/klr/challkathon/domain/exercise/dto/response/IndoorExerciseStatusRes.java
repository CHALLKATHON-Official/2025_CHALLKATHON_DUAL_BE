package kr.klr.challkathon.domain.exercise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndoorExerciseStatusRes {
    
    private TodayProgress todayProgress;
    private List<ExerciseItem> requiredExercises;
    private List<ExerciseItem> recommendedExercises;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TodayProgress {
        private Integer completedRequiredExercises;
        private Integer totalExerciseMinutes;
        private Double requiredExerciseCompletionRate; // 0.0 ~ 1.0
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExerciseItem {
        private Long exerciseId;
        private String name;
        private String description;
        private Boolean isCompleted;
        private Boolean isRequired;
        private Integer orderIndex;
    }
}
