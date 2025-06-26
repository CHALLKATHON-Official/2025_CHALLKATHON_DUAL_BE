package kr.klr.challkathon.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardianPatientLinkReq {
    
    @NotBlank(message = "환자 UID는 필수입니다")
    private String patientUid;
}
