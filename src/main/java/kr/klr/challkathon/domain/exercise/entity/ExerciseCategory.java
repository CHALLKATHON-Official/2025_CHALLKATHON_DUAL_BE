package kr.klr.challkathon.domain.exercise.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExerciseCategory {
    REQUIRED("필수 운동"),
    RECOMMENDED("함께하면 좋아요");
    
    private final String displayName;
}
