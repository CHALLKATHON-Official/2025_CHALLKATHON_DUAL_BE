package kr.klr.challkathon.domain.health.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import kr.klr.challkathon.domain.health.dto.request.PainRecordReq;
import kr.klr.challkathon.domain.health.dto.response.PainRecordHistoryRes;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Tag(name = "Health", description = "건강 관련 API")
public interface HealthControllerSpec {

    @Operation(summary = "수동 통증 기록", description = "사용자가 직접 통증을 기록합니다.")
    GlobalApiResponse<String> recordPain(
            @Parameter(hidden = true) String userUid,
            @Valid @RequestBody PainRecordReq request);

    @Operation(summary = "운동 후 통증 기록", description = "운동 직후 통증을 기록합니다.")
    GlobalApiResponse<String> recordPainAfterExercise(
            @Parameter(hidden = true) String userUid,
            @Valid @RequestBody PainRecordReq request);

    @Operation(summary = "통증 기록 히스토리", description = "통증 기록 히스토리를 조회합니다.")
    GlobalApiResponse<PainRecordHistoryRes> getPainHistory(
            @Parameter(hidden = true) String userUid,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
}