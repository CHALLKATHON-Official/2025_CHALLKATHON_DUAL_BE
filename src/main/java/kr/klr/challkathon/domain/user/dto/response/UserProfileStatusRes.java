package kr.klr.challkathon.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileStatusRes {
    
    private Boolean isPatientProfileComplete;
    private Boolean isGuardianProfileComplete;
    
    // 환자 정보 (완성된 경우)
    private PatientProfile patientProfile;
    
    // 보호자 정보 (완성된 경우)
    private GuardianProfile guardianProfile;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientProfile {
        private Integer age;
        private String disease;
        private String phoneNumber;
        private String emergencyContact;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GuardianProfile {
        private String targetPatientName;
        private String targetPatientUid;
    }
}
