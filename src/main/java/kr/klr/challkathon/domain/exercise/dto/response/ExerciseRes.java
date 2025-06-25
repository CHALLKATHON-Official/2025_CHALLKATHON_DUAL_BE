package kr.klr.challkathon.domain.exercise.dto.response;

import kr.klr.challkathon.domain.exercise.entity.Exercise;
import kr.klr.challkathon.domain.exercise.entity.ExerciseCategory;
import kr.klr.challkathon.domain.exercise.entity.ExerciseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRes {
    
    private Long id;
    private String name;
    private String description;
    private ExerciseCategory category;
    private String categoryDisplayName;
    private ExerciseType type;
    private String typeDisplayName;
    private Integer durationMinutes;
    private Double caloriesPerMinute;
    private Boolean isRequired;
    private Boolean isCompleted; // 오늘 완료 여부
    
    public static ExerciseRes fromEntity(Exercise exercise) {
        return ExerciseRes.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .description(exercise.getDescription())
                .category(exercise.getCategory())
                .categoryDisplayName(exercise.getCategory().getDisplayName())
                .type(exercise.getType())
                .typeDisplayName(exercise.getType().getDisplayName())
                .durationMinutes(exercise.getDurationMinutes())
                .caloriesPerMinute(exercise.getCaloriesPerMinute())
                .isRequired(exercise.getIsRequired())
                .isCompleted(false) // 기본값, 서비스에서 설정
                .build();
    }
    
    public static ExerciseRes fromEntity(Exercise exercise, boolean isCompleted) {
        ExerciseRes response = fromEntity(exercise);
        response.isCompleted = isCompleted;
        return response;
    }
}
