package kr.klr.challkathon.domain.health.controller;

import kr.klr.challkathon.domain.health.dto.request.PainRecordReq;
import kr.klr.challkathon.domain.health.dto.response.PainRecordHistoryRes;
import kr.klr.challkathon.domain.health.service.HealthService;
import kr.klr.challkathon.domain.health.spec.HealthControllerSpec;
import kr.klr.challkathon.global.customAnnotation.CurrentUser;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController implements HealthControllerSpec {

    private final HealthService healthService;

    @PostMapping("/pain/record")
    @Override
    public GlobalApiResponse<String> recordPain(
            @CurrentUser String userUid,
            @Valid @RequestBody PainRecordReq request) {
        String message = healthService.recordPainManually(userUid, request);
        return GlobalApiResponse.success(message);
    }

    @PostMapping("/pain/record/after-exercise")
    @Override
    public GlobalApiResponse<String> recordPainAfterExercise(
            @CurrentUser String userUid,
            @Valid @RequestBody PainRecordReq request) {
        String message = healthService.recordPainAfterExercise(userUid, request);
        return GlobalApiResponse.success(message);
    }

    @GetMapping("/pain/history")
    @Override
    public GlobalApiResponse<PainRecordHistoryRes> getPainHistory(
            @CurrentUser String userUid,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        PainRecordHistoryRes response = healthService.getPainRecordHistory(userUid, startDate, endDate);
        return GlobalApiResponse.success(response);
    }
}
