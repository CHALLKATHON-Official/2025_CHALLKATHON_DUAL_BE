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
public class ExerciseListRes {
    
    private List<ExerciseRes> requiredExercises;  // 필수 운동
    private List<ExerciseRes> recommendedExercises;  // 함께하면 좋아요
    private ExerciseSummary summary;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExerciseSummary {
        private Integer totalExercises;
        private Integer completedExercises;
        private Integer completedRequiredExercises;
        private Integer totalRequiredExercises;
        private Double completionRate;
    }
}
