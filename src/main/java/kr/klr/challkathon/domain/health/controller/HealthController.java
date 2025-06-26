package kr.klr.challkathon.domain.health.controller;

import kr.klr.challkathon.domain.health.dto.request.PainRecordReq;
import kr.klr.challkathon.domain.health.dto.response.PainRecordHistoryRes;
import kr.klr.challkathon.domain.health.entity.HealthRecord;
import kr.klr.challkathon.domain.health.service.HealthService;
import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.global.customAnnotation.CurrentUser;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
@Tag(name = "Health", description = "건강 관련 API")
public class HealthController {

    private final HealthService healthService;

    @PostMapping("/pain/record")
    @Operation(summary = "수동 통증 기록", description = "사용자가 직접 통증을 기록합니다.")
    public GlobalApiResponse<String> recordPain(
            @CurrentUser User user,
            @Valid @RequestBody PainRecordReq request) {
        String message = healthService.recordPainManually(user, request);
        return GlobalApiResponse.success(message);
    }

    @PostMapping("/pain/record/after-exercise")
    @Operation(summary = "운동 후 통증 기록", description = "운동 직후 통증을 기록합니다.")
    public GlobalApiResponse<String> recordPainAfterExercise(
            @CurrentUser User user,
            @Valid @RequestBody PainRecordReq request) {
        String message = healthService.recordPainAfterExercise(user, request);
        return GlobalApiResponse.success(message);
    }

    @GetMapping("/pain/history")
    @Operation(summary = "통증 기록 히스토리", description = "통증 기록 히스토리를 조회합니다.")
    public GlobalApiResponse<PainRecordHistoryRes> getPainHistory(
            @CurrentUser User user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        PainRecordHistoryRes response = healthService.getPainRecordHistory(user, startDate, endDate);
        return GlobalApiResponse.success(response);
    }
}
