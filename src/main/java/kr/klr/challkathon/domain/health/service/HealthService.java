package kr.klr.challkathon.domain.health.service;

import kr.klr.challkathon.domain.guardian.entity.GuardianAlert;
import kr.klr.challkathon.domain.guardian.repository.GuardianAlertRepository;
import kr.klr.challkathon.domain.health.dto.request.PainRecordReq;
import kr.klr.challkathon.domain.health.dto.response.PainRecordHistoryRes;
import kr.klr.challkathon.domain.health.entity.HealthRecord;
import kr.klr.challkathon.domain.health.repository.HealthRecordRepository;
import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.domain.user.repository.UserRepository;
import kr.klr.challkathon.global.globalResponse.error.ErrorCode;
import kr.klr.challkathon.global.globalResponse.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HealthService {

    private final HealthRecordRepository healthRecordRepository;
    private final GuardianAlertRepository guardianAlertRepository;
    private final UserRepository userRepository;

    @Transactional
    public String recordPainManually(String userUid, PainRecordReq request) {
        User user = getUserByUid(userUid);
        
        HealthRecord record = HealthRecord.builder()
                .user(user)
                .recordDate(LocalDate.now())
                .recordTime(LocalDateTime.now())
                .legPainScore(request.getLegPainScore())
                .kneePainScore(request.getKneePainScore())
                .anklePainScore(request.getAnklePainScore())
                .heelPainScore(request.getHeelPainScore())
                .backPainScore(request.getBackPainScore())
                .recordType(HealthRecord.PainRecordType.MANUAL)
                .notes(request.getNotes())
                .build();

        HealthRecord saved = healthRecordRepository.save(record);
        
        // 통증 증가 알림 체크
        checkPainIncreaseAlert(user, saved);
        
        return "통증이 기록되었습니다. (총 " + saved.getTotalPainScore() + "/15점)";
    }

    @Transactional
    public String recordPainAfterExercise(String userUid, PainRecordReq request) {
        User user = getUserByUid(userUid);
        
        HealthRecord record = HealthRecord.builder()
                .user(user)
                .recordDate(LocalDate.now())
                .recordTime(LocalDateTime.now())
                .legPainScore(request.getLegPainScore())
                .kneePainScore(request.getKneePainScore())
                .anklePainScore(request.getAnklePainScore())
                .heelPainScore(request.getHeelPainScore())
                .backPainScore(request.getBackPainScore())
                .recordType(HealthRecord.PainRecordType.POST_EXERCISE)
                .notes(request.getNotes())
                .build();

        HealthRecord saved = healthRecordRepository.save(record);
        
        // 통증 증가 알림 체크
        checkPainIncreaseAlert(user, saved);
        
        return "운동 후 통증이 기록되었습니다. (총 " + saved.getTotalPainScore() + "/15점)";
    }

    public PainRecordHistoryRes getPainRecordHistory(String userUid, LocalDate startDate, LocalDate endDate) {
        User user = getUserByUid(userUid);
        
        List<HealthRecord> records;
        
        if (startDate != null && endDate != null) {
            records = healthRecordRepository.findByUserAndRecordDateBetweenOrderByRecordTimeDesc(user, startDate, endDate);
        } else {
            records = healthRecordRepository.findByUserOrderByRecordTimeDesc(user);
        }
        
        List<PainRecordHistoryRes.PainRecordItem> painRecords = records.stream()
                .map(record -> PainRecordHistoryRes.PainRecordItem.builder()
                        .recordId(record.getRecordId())
                        .recordDate(record.getRecordDate())
                        .recordTime(record.getRecordTime())
                        .recordType(record.getRecordType())
                        .totalPainScore(record.getTotalPainScore())
                        .notes(record.getNotes())
                        .painScores(PainRecordHistoryRes.PainScores.builder()
                                .legPainScore(record.getLegPainScore())
                                .kneePainScore(record.getKneePainScore())
                                .anklePainScore(record.getAnklePainScore())
                                .heelPainScore(record.getHeelPainScore())
                                .backPainScore(record.getBackPainScore())
                                .build())
                        .build())
                .collect(Collectors.toList());
        
        return PainRecordHistoryRes.builder()
                .painRecords(painRecords)
                .build();
    }

    public Optional<HealthRecord> getTodayLatestPainRecord(User user) {
        return healthRecordRepository.findTopByUserAndRecordDateOrderByRecordTimeDesc(user, LocalDate.now());
    }

    private User getUserByUid(String userUid) {
        return userRepository.findByUid(userUid)
                .orElseThrow(() -> new GlobalException(ErrorCode.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
    }

    private void checkPainIncreaseAlert(User user, HealthRecord currentRecord) {
        // 환자 프로필이 완성되지 않은 경우 알림 처리하지 않음
        if (!user.getIsPatientProfileComplete()) {
            return;
        }
        
        // 이전 통증 기록 조회 (오늘 이전의 최신 기록)
        List<HealthRecord> previousRecords = healthRecordRepository.findByUserOrderByRecordTimeDesc(user);
        
        Optional<HealthRecord> previousRecordOpt = previousRecords.stream()
                .filter(record -> record.getRecordTime().isBefore(currentRecord.getRecordTime()))
                .findFirst();
        
        if (previousRecordOpt.isEmpty()) {
            return; // 이전 기록이 없으면 알림 생성하지 않음
        }
        
        HealthRecord previousRecord = previousRecordOpt.get();
        int previousPainLevel = previousRecord.getTotalPainScore();
        int currentPainLevel = currentRecord.getTotalPainScore();
        
        // 통증이 3점 이상 증가한 경우 알림 생성
        if (currentPainLevel - previousPainLevel >= 3) {
            createPainIncreaseAlert(user, previousPainLevel, currentPainLevel);
        }
    }

    private void createPainIncreaseAlert(User patient, int previousPainLevel, int currentPainLevel) {
        // 해당 환자의 보호자들 찾기
        List<User> guardians = userRepository.findGuardiansByPatient(patient);
        
        for (User guardian : guardians) {
            GuardianAlert alert = GuardianAlert.builder()
                    .guardian(guardian)
                    .patient(patient)
                    .alertType(GuardianAlert.AlertType.PAIN_INCREASE)
                    .alertTime(LocalDateTime.now())
                    .previousPainLevel(previousPainLevel)
                    .currentPainLevel(currentPainLevel)
                    .message(String.format("%s님의 통증이 %d점에서 %d점으로 증가했습니다.", 
                            patient.getNickname() != null ? patient.getNickname() : patient.getUsername(),
                            previousPainLevel, currentPainLevel))
                    .build();
            
            guardianAlertRepository.save(alert);
        }
    }
}
