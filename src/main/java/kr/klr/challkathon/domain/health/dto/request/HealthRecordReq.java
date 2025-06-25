package kr.klr.challkathon.domain.health.dto.request;

import kr.klr.challkathon.domain.health.entity.OverallCondition;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthRecordReq {
    
    @Min(value = 0, message = "통증 점수는 0점 이상이어야 합니다")
    @Max(value = 10, message = "통증 점수는 10점 이하여야 합니다")
    private Integer chestPainScore;
    
    @Min(value = 0, message = "통증 점수는 0점 이상이어야 합니다")
    @Max(value = 10, message = "통증 점수는 10점 이하여야 합니다")
    private Integer backPainScore;
    
    @Min(value = 0, message = "통증 점수는 0점 이상이어야 합니다")
    @Max(value = 10, message = "통증 점수는 10점 이하여야 합니다")
    private Integer waistPainScore;
    
    @Min(value = 0, message = "통증 점수는 0점 이상이어야 합니다")
    @Max(value = 10, message = "통증 점수는 10점 이하여야 합니다")
    private Integer neckPainScore;
    
    @Min(value = 0, message = "통증 점수는 0점 이상이어야 합니다")
    @Max(value = 10, message = "통증 점수는 10점 이하여야 합니다")
    private Integer legPainScore;
    
    private OverallCondition overallCondition;
    private String notes;
}
