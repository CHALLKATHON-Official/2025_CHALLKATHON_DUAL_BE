package kr.klr.challkathon.domain.health.dto.response;

import kr.klr.challkathon.domain.health.entity.HealthRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PainRecordHistoryRes {
    
    private List<PainRecordItem> painRecords;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PainRecordItem {
        private String recordId;
        private LocalDate recordDate;
        private LocalDateTime recordTime;
        private HealthRecord.PainRecordType recordType; // POST_EXERCISE, MANUAL
        private Integer totalPainScore; // 15점 만점
        private String notes; // 메모(상세 기록)
        private PainScores painScores;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PainScores {
        private Integer legPainScore;    // 다리
        private Integer kneePainScore;   // 무릎
        private Integer anklePainScore;  // 발목
        private Integer heelPainScore;   // 뒷꿈치
        private Integer backPainScore;   // 허리
    }
}
