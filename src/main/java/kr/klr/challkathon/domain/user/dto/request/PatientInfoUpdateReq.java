package kr.klr.challkathon.domain.user.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientInfoUpdateReq {
    
    @NotNull(message = "나이는 필수입니다")
    @Min(value = 1, message = "나이는 1 이상이어야 합니다")
    private Integer age;
    
    @NotBlank(message = "질환 정보는 필수입니다")
    private String disease;
    
    @NotBlank(message = "전화번호는 필수입니다")
    private String phoneNumber;
    
    private String emergencyContact; // 선택사항
}
