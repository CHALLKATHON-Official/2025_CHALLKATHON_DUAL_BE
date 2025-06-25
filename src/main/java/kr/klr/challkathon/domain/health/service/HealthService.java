package kr.klr.challkathon.domain.health.service;

import kr.klr.challkathon.domain.health.dto.request.HealthRecordReq;
import kr.klr.challkathon.domain.health.entity.HealthRecord;
import kr.klr.challkathon.domain.health.repository.HealthRecordRepository;
import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HealthService {
    
    private final HealthRecordRepository healthRecordRepository;
    private final UserService userService;
    
    /**
     * 건강상태 기록 저장/업데이트
     */
    @Transactional
    public HealthRecord recordHealthStatus(String userUid, HealthRecordReq request) {
        User user = userService.findByUid(userUid);
        LocalDate today = LocalDate.now();
        
        // 오늘 날짜의 기록이 있는지 확인
        HealthRecord record = healthRecordRepository.findByUserAndRecordDate(user, today)
                .orElse(HealthRecord.builder()
                        .user(user)
                        .recordDate(today)
                        .build());
        
        // 데이터 업데이트
        record.setChestPainScore(request.getChestPainScore());
        record.setBackPainScore(request.getBackPainScore());
        record.setWaistPainScore(request.getWaistPainScore());
        record.setNeckPainScore(request.getNeckPainScore());
        record.setLegPainScore(request.getLegPainScore());
        record.setOverallCondition(request.getOverallCondition());
        record.setNotes(request.getNotes());
        
        HealthRecord savedRecord = healthRecordRepository.save(record);
        
        log.info("건강상태 기록 저장 완료: user={}, date={}", userUid, today);
        
        return savedRecord;
    }
    
    /**
     * 최근 7일 건강 기록 조회
     */
    public List<HealthRecord> getRecentHealthRecords(String userUid) {
        User user = userService.findByUid(userUid);
        return healthRecordRepository.findRecentRecordsByUser(user);
    }
    
    /**
     * 특정 기간 건강 기록 조회
     */
    public List<HealthRecord> getHealthRecords(String userUid, LocalDate startDate, LocalDate endDate) {
        User user = userService.findByUid(userUid);
        return healthRecordRepository.findByUserAndRecordDateBetweenOrderByRecordDateDesc(
                user, startDate, endDate);
    }
}
