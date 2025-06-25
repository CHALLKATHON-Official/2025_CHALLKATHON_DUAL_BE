package kr.klr.challkathon.domain.exercise.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExerciseType {
    INDOOR("실내운동"),
    OUTDOOR("실외운동");
    
    private final String displayName;
}
