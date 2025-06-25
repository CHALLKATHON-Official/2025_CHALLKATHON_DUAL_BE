package kr.klr.challkathon.domain.health.controller;

import jakarta.validation.Valid;
import kr.klr.challkathon.domain.health.dto.request.HealthRecordReq;
import kr.klr.challkathon.domain.health.entity.HealthRecord;
import kr.klr.challkathon.domain.health.service.HealthService;
import kr.klr.challkathon.global.customAnnotation.CurrentUser;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.*;
import kr.klr.challkathon.domain.health.spec.HealthControllerSpec;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
public class HealthController implements HealthControllerSpec {
    
    private final HealthService healthService;
    
    @Operation(summary = "건강상태 기록", description = "운동 후 건강상태/통증을 기록합니다.")
    @PostMapping("/record")
    public GlobalApiResponse<HealthRecord> recordHealthStatus(
            @CurrentUser String userUid,
            @Valid @RequestBody HealthRecordReq healthRecordReq) {
        
        HealthRecord response = healthService.recordHealthStatus(userUid, healthRecordReq);
        
        return GlobalApiResponse.success(response);
    }
    
    @Operation(summary = "최근 건강 기록 조회", description = "최근 7일간의 건강 기록을 조회합니다.")
    @GetMapping("/records/recent")
    public GlobalApiResponse<List<HealthRecord>> getRecentHealthRecords(
            @CurrentUser String userUid) {
        
        List<HealthRecord> response = healthService.getRecentHealthRecords(userUid);
        
        return GlobalApiResponse.success(response);
    }
    
    @Operation(summary = "건강 기록 조회", description = "특정 기간의 건강 기록을 조회합니다.")
    @GetMapping("/records")
    public GlobalApiResponse<List<HealthRecord>> getHealthRecords(
            @CurrentUser String userUid,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
        
        List<HealthRecord> response = healthService.getHealthRecords(userUid, startDate, endDate);
        
        return GlobalApiResponse.success(response);
    }
}
