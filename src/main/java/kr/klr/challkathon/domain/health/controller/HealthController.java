package kr.klr.challkathon.domain.health.controller;

import kr.klr.challkathon.domain.auth.utils.AuthUtil;
import kr.klr.challkathon.domain.health.dto.request.HealthRecordReq;
import kr.klr.challkathon.domain.health.entity.HealthRecord;
import kr.klr.challkathon.domain.health.service.HealthService;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
@Tag(name = "Health", description = "건강상태 기록 관련 API")
public class HealthController {
    
    private final HealthService healthService;
    private final AuthUtil authUtil;
    
    @Operation(summary = "건강상태 기록", description = "운동 후 건강상태/통증을 기록합니다.")
    @PostMapping("/record")
    public ResponseEntity<GlobalApiResponse<HealthRecord>> recordHealthStatus(
            HttpServletRequest request,
            @Valid @RequestBody HealthRecordReq healthRecordReq) {
        
        String userUid = authUtil.getCurrentUserUid(request);
        HealthRecord response = healthService.recordHealthStatus(userUid, healthRecordReq);
        
        return ResponseEntity.ok(GlobalApiResponse.success(response));
    }
    
    @Operation(summary = "최근 건강 기록 조회", description = "최근 7일간의 건강 기록을 조회합니다.")
    @GetMapping("/records/recent")
    public ResponseEntity<GlobalApiResponse<List<HealthRecord>>> getRecentHealthRecords(
            HttpServletRequest request) {
        
        String userUid = authUtil.getCurrentUserUid(request);
        List<HealthRecord> response = healthService.getRecentHealthRecords(userUid);
        
        return ResponseEntity.ok(GlobalApiResponse.success(response));
    }
    
    @Operation(summary = "건강 기록 조회", description = "특정 기간의 건강 기록을 조회합니다.")
    @GetMapping("/records")
    public ResponseEntity<GlobalApiResponse<List<HealthRecord>>> getHealthRecords(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        String userUid = authUtil.getCurrentUserUid(request);
        List<HealthRecord> response = healthService.getHealthRecords(userUid, startDate, endDate);
        
        return ResponseEntity.ok(GlobalApiResponse.success(response));
    }
}
