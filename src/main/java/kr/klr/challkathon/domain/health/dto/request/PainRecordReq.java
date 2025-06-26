package kr.klr.challkathon.domain.health.dto.request;

import jakarta.validation.constraints.Max;
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
public class PainRecordReq {
    
    @NotNull(message = "다리 통증 점수는 필수입니다")
    @Min(value = 0, message = "통증 점수는 0 이상이어야 합니다")
    @Max(value = 3, message = "통증 점수는 3 이하이어야 합니다")
    private Integer legPainScore;
    
    @NotNull(message = "무릎 통증 점수는 필수입니다")
    @Min(value = 0, message = "통증 점수는 0 이상이어야 합니다")
    @Max(value = 3, message = "통증 점수는 3 이하이어야 합니다")
    private Integer kneePainScore;
    
    @NotNull(message = "발목 통증 점수는 필수입니다")
    @Min(value = 0, message = "통증 점수는 0 이상이어야 합니다")
    @Max(value = 3, message = "통증 점수는 3 이하이어야 합니다")
    private Integer anklePainScore;
    
    @NotNull(message = "뒷꿈치 통증 점수는 필수입니다")
    @Min(value = 0, message = "통증 점수는 0 이상이어야 합니다")
    @Max(value = 3, message = "통증 점수는 3 이하이어야 합니다")
    private Integer heelPainScore;
    
    @NotNull(message = "허리 통증 점수는 필수입니다")
    @Min(value = 0, message = "통증 점수는 0 이상이어야 합니다")
    @Max(value = 3, message = "통증 점수는 3 이하이어야 합니다")
    private Integer backPainScore;
    
    private String notes; // 상세 기록
}
