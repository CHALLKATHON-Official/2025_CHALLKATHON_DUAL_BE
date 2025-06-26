package kr.klr.challkathon.domain.exercise.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkingExerciseRecordReq {
    
    @NotNull(message = "운동 ID는 필수입니다")
    private Long exerciseId;
    
    @NotNull(message = "운동 시간은 필수입니다")
    @Min(value = 1, message = "운동 시간은 1분 이상이어야 합니다")
    private Integer durationMinutes;
    
    private Integer steps; // 걸음 수
    private Double distanceKm; // 거리
    private Double caloriesBurned; // 소모 칼로리
    
    private String notes; // 메모
}
