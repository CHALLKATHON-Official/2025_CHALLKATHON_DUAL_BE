package kr.klr.challkathon.domain.health.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OverallCondition {
    EXCELLENT("좋음"),
    GOOD("보통"),
    FAIR("나쁨"),
    POOR("매우 나쁨");
    
    private final String displayName;
}
