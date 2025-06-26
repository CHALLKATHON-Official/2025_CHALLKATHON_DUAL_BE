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
public class PatientDetailRes {
    
    private PatientInfo patientInfo;
    private ContactInfo contactInfo;
    private WeeklyProgress weeklyProgress;
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientInfo {
        private String patientName;
        private Integer patientAge;
        private String disease;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactInfo {
        private String phoneNumber;
        private String emergencyContact;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklyProgress {
        private List<DailyStepData> dailySteps; // 월~일
        private Integer totalSteps;
    }
    
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyStepData {
        private String dayOfWeek; // MON, TUE, WED, THU, FRI, SAT, SUN
        private String dayName; // 월, 화, 수, 목, 금, 토, 일
        private Integer steps;
    }
}
