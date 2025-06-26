package kr.klr.challkathon.domain.guardian.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientLinkReq {
    
    @NotBlank(message = "환자 연동 코드는 필수입니다")
    private String patientLinkCode; // 환자가 생성한 6자리 연동 코드
}
