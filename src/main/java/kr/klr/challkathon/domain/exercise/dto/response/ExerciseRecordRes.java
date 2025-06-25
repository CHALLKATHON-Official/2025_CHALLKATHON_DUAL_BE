package kr.klr.challkathon.domain.exercise.dto.response;

import kr.klr.challkathon.domain.exercise.entity.CompletionStatus;
import kr.klr.challkathon.domain.exercise.entity.ExerciseRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRecordRes {
    
    private String recordId;
    private Long exerciseId;
    private String exerciseName;
    private LocalDate exerciseDate;
    private Integer durationMinutes;
    private Double caloriesBurned;
    private Integer steps;
    private Double distanceKm;
    private CompletionStatus completionStatus;
    private String notes;
    private LocalDateTime createdAt;
    
    public static ExerciseRecordRes fromEntity(ExerciseRecord record) {
        return ExerciseRecordRes.builder()
                .recordId(record.getRecordId())
                .exerciseId(record.getExercise().getId())
                .exerciseName(record.getExercise().getName())
                .exerciseDate(record.getExerciseDate())
                .durationMinutes(record.getDurationMinutes())
                .caloriesBurned(record.getCaloriesBurned())
                .steps(record.getSteps())
                .distanceKm(record.getDistanceKm())
                .completionStatus(record.getCompletionStatus())
                .notes(record.getNotes())
                .createdAt(record.getCreatedAt())
                .build();
    }
}
