package kr.klr.challkathon.domain.guardian.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardianDashboardRes {
    
    private GuardianProfile guardianProfile;
    private PatientInfo patientInfo;
    private TodayStatus todayStatus;
    private List<EmergencyAlert> emergencyAlerts;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GuardianProfile {
        private String guardianName;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientInfo {
        private String patientName;
        private Integer patientAge;
        private String disease;
        private String phoneNumber;
        private String emergencyContact;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TodayStatus {
        private Integer steps;
        private Integer exerciseMinutes;
        private String painLevel; // "3/15" 형태
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmergencyAlert {
        private String alertId;
        private String alertType; // PAIN_INCREASE
        private String message;
        private String timeSince; // "5분 전"
        private Integer previousPainLevel;
        private Integer currentPainLevel;
        private String patientPhoneNumber;
    }
}
