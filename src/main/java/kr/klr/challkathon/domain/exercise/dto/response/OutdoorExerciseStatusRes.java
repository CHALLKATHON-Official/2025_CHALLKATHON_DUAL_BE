package kr.klr.challkathon.domain.exercise.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutdoorExerciseStatusRes {
    
    private Double maxDistanceRecord; // 최고 거리 기록
    private YesterdayRecord yesterdayRecord;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class YesterdayRecord {
        private Integer durationMinutes;
        private Double distanceKm;
    }
}
