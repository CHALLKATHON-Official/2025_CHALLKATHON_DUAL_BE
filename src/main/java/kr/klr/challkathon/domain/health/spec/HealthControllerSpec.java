package kr.klr.challkathon.domain.health.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import kr.klr.challkathon.domain.health.dto.request.HealthRecordReq;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Health", description = "건강상태 기록 관련 API")
public interface HealthControllerSpec {

    @Operation(summary = "건강상태 기록", description = "운동 후 건강상태/통증을 기록합니다.")
    @PostMapping("/api/v1/health/record")
    GlobalApiResponse<?> recordHealthStatus(
            @Parameter(hidden = true) String userUid,
            HealthRecordReq healthRecordReq);

    @Operation(summary = "최근 건강 기록 조회", description = "최근 7일간의 건강 기록을 조회합니다.")
    @GetMapping("/api/v1/health/records/recent")
    GlobalApiResponse<?> getRecentHealthRecords(
            @Parameter(hidden = true) String userUid);

    @Operation(summary = "건강 기록 조회", description = "특정 기간의 건강 기록을 조회합니다.")
    @GetMapping("/api/v1/health/records")
    GlobalApiResponse<?> getHealthRecords(
            @Parameter(hidden = true) String userUid,
            LocalDate startDate,
            LocalDate endDate);
} 