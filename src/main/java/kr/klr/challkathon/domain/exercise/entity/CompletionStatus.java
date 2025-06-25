package kr.klr.challkathon.domain.exercise.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompletionStatus {
    COMPLETED("완료"),
    IN_PROGRESS("진행중"),
    PAUSED("일시정지"),
    CANCELLED("취소");
    
    private final String displayName;
}
