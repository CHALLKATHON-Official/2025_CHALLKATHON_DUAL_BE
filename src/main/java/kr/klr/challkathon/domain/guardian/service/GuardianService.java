package kr.klr.challkathon.domain.guardian.service;

import kr.klr.challkathon.domain.dashboard.entity.DailyStats;
import kr.klr.challkathon.domain.dashboard.repository.DailyStatsRepository;
import kr.klr.challkathon.domain.guardian.dto.response.GuardianDashboardRes;
import kr.klr.challkathon.domain.guardian.dto.response.PatientDetailRes;
import kr.klr.challkathon.domain.guardian.entity.GuardianAlert;
import kr.klr.challkathon.domain.guardian.repository.GuardianAlertRepository;
import kr.klr.challkathon.domain.health.entity.HealthRecord;
import kr.klr.challkathon.domain.health.repository.HealthRecordRepository;
import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.domain.user.service.UserService;
import kr.klr.challkathon.global.globalResponse.error.ErrorCode;
import kr.klr.challkathon.global.globalResponse.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuardianService {

    private final UserService userService;
    private final GuardianAlertRepository guardianAlertRepository;
    private final DailyStatsRepository dailyStatsRepository;
    private final HealthRecordRepository healthRecordRepository;

    /**
     * 보호자 대시보드 조회
     */
    public GuardianDashboardRes getGuardianDashboard(String guardianUid) {
        User guardian = userService.findByUid(guardianUid);
        validateGuardian(guardian);
        
        User patient = guardian.getGuardianTargetPatient();
        if (patient == null) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "연동된 환자가 없습니다. 먼저 환자와 연동해주세요.");
        }
        
        // 보호자 프로필
        GuardianDashboardRes.GuardianProfile guardianProfile = GuardianDashboardRes.GuardianProfile.builder()
                .guardianName(getDisplayName(guardian))
                .build();
        
        // 환자 정보
        GuardianDashboardRes.PatientInfo patientInfo = GuardianDashboardRes.PatientInfo.builder()
                .patientName(getDisplayName(patient))
                .patientAge(patient.getPatientAge())
                .disease(patient.getPatientDisease())
                .phoneNumber(patient.getPatientPhoneNumber())
                .emergencyContact(patient.getPatientEmergencyContact())
                .build();
        
        // 오늘의 현황
        GuardianDashboardRes.TodayStatus todayStatus = getTodayStatus(patient);
        
        // 긴급 알림
        List<GuardianDashboardRes.EmergencyAlert> emergencyAlerts = getEmergencyAlerts(guardian);
        
        return GuardianDashboardRes.builder()
                .guardianProfile(guardianProfile)
                .patientInfo(patientInfo)
                .todayStatus(todayStatus)
                .emergencyAlerts(emergencyAlerts)
                .build();
    }

    /**
     * 환자 상세 정보 조회
     */
    public PatientDetailRes getPatientDetail(String guardianUid) {
        User guardian = userService.findByUid(guardianUid);
        validateGuardian(guardian);
        
        User patient = guardian.getGuardianTargetPatient();
        if (patient == null) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "연동된 환자가 없습니다.");
        }
        
        // 환자 정보
        PatientDetailRes.PatientInfo patientInfo = PatientDetailRes.PatientInfo.builder()
                .patientName(getDisplayName(patient))
                .patientAge(patient.getPatientAge())
                .disease(patient.getPatientDisease())
                .build();
        
        // 연락처 정보
        PatientDetailRes.ContactInfo contactInfo = PatientDetailRes.ContactInfo.builder()
                .phoneNumber(patient.getPatientPhoneNumber())
                .emergencyContact(patient.getPatientEmergencyContact())
                .build();
        
        // 이번주 진행 상황
        PatientDetailRes.WeeklyProgress weeklyProgress = getWeeklyProgress(patient);
        
        return PatientDetailRes.builder()
                .patientInfo(patientInfo)
                .contactInfo(contactInfo)
                .weeklyProgress(weeklyProgress)
                .build();
    }

    /**
     * 알림 읽음 처리
     */
    @Transactional
    public void markAlertAsRead(String guardianUid, String alertId) {
        User guardian = userService.findByUid(guardianUid);
        
        GuardianAlert alert = guardianAlertRepository.findById(alertId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "알림을 찾을 수 없습니다."));
        
        if (!alert.getGuardian().getUid().equals(guardian.getUid())) {
            throw new GlobalException(ErrorCode.FORBIDDEN, "해당 알림에 접근할 권한이 없습니다.");
        }
        
        alert.setIsRead(true);
        guardianAlertRepository.save(alert);
    }

    /**
     * 보호자 권한 검증
     */
    private void validateGuardian(User guardian) {
        if (!guardian.getIsGuardianProfileComplete()) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "먼저 환자와 연동하여 보호자 프로필을 설정해주세요.");
        }
    }

    /**
     * 사용자 표시 이름 가져오기
     */
    private String getDisplayName(User user) {
        return user.getNickname() != null ? user.getNickname() : user.getUsername();
    }

    /**
     * 오늘의 환자 현황 조회
     */
    private GuardianDashboardRes.TodayStatus getTodayStatus(User patient) {
        LocalDate today = LocalDate.now();
        
        // 오늘의 통계 조회
        Optional<DailyStats> todayStatsOpt = dailyStatsRepository.findByUserAndStatsDate(patient, today);
        
        int steps = todayStatsOpt.map(DailyStats::getTotalSteps).orElse(0);
        int exerciseMinutes = todayStatsOpt.map(DailyStats::getTotalExerciseMinutes).orElse(0);
        
        // 오늘의 통증 수준
        Optional<HealthRecord> latestPainRecord = healthRecordRepository.findTopByUserAndRecordDateOrderByRecordTimeDesc(patient, today);
        String painLevel = latestPainRecord
                .map(record -> record.getTotalPainScore() + "/15")
                .orElse("0/15");
        
        return GuardianDashboardRes.TodayStatus.builder()
                .steps(steps)
                .exerciseMinutes(exerciseMinutes)
                .painLevel(painLevel)
                .build();
    }

    /**
     * 긴급 알림 목록 조회
     */
    private List<GuardianDashboardRes.EmergencyAlert> getEmergencyAlerts(User guardian) {
        // 최근 24시간 내의 읽지 않은 알림만 조회
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        List<GuardianAlert> alerts = guardianAlertRepository.findRecentAlertsByGuardian(guardian, since);
        
        return alerts.stream()
                .filter(alert -> !alert.getIsRead())
                .map(alert -> GuardianDashboardRes.EmergencyAlert.builder()
                        .alertId(alert.getAlertId())
                        .alertType(alert.getAlertType().name())
                        .message(alert.getMessage())
                        .timeSince(getTimeSince(alert.getAlertTime()))
                        .previousPainLevel(alert.getPreviousPainLevel())
                        .currentPainLevel(alert.getCurrentPainLevel())
                        .patientPhoneNumber(alert.getPatient().getPatientPhoneNumber())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 주간 진행 상황 조회
     */
    private PatientDetailRes.WeeklyProgress getWeeklyProgress(User patient) {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);
        
        List<DailyStats> weeklyStats = dailyStatsRepository.findByUserAndStatsDateBetween(patient, monday, sunday);
        
        List<PatientDetailRes.DailyStepData> dailySteps = new ArrayList<>();
        int totalSteps = 0;
        
        for (int i = 0; i < 7; i++) {
            LocalDate date = monday.plusDays(i);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            
            Optional<DailyStats> statsOpt = weeklyStats.stream()
                    .filter(stats -> stats.getStatsDate().equals(date))
                    .findFirst();
            
            int steps = statsOpt.map(DailyStats::getTotalSteps).orElse(0);
            totalSteps += steps;
            
            dailySteps.add(PatientDetailRes.DailyStepData.builder()
                    .dayOfWeek(dayOfWeek.name())
                    .dayName(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN))
                    .steps(steps)
                    .build());
        }
        
        return PatientDetailRes.WeeklyProgress.builder()
                .dailySteps(dailySteps)
                .totalSteps(totalSteps)
                .build();
    }

    /**
     * 시간 경과 계산
     */
    private String getTimeSince(LocalDateTime alertTime) {
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(alertTime, now);
        
        if (minutes < 1) {
            return "방금 전";
        } else if (minutes < 60) {
            return minutes + "분 전";
        } else {
            long hours = minutes / 60;
            if (hours < 24) {
                return hours + "시간 전";
            } else {
                long days = hours / 24;
                return days + "일 전";
            }
        }
    }
}
