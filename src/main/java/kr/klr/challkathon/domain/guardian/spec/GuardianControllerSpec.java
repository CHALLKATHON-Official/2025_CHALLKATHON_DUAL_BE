package kr.klr.challkathon.domain.guardian.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.klr.challkathon.domain.guardian.dto.request.PatientLinkReq;
import kr.klr.challkathon.domain.guardian.dto.response.GuardianDashboardRes;
import kr.klr.challkathon.domain.guardian.dto.response.PatientDetailRes;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "Guardian", description = "보호자 관련 API")
public interface GuardianControllerSpec {

    @Operation(summary = "환자 연동", description = "환자가 제공한 6자리 연동 코드로 환자와 연동하여 보호자 프로필을 생성합니다.")
    GlobalApiResponse<String> linkPatient(
            @Parameter(hidden = true) String userUid,
            @Valid @RequestBody PatientLinkReq request);

    @Operation(summary = "보호자 메인페이지 조회", description = "보호자 대시보드 정보를 조회합니다.")
    GlobalApiResponse<GuardianDashboardRes> getGuardianDashboard(
            @Parameter(hidden = true) String userUid);

    @Operation(summary = "환자 정보 페이지 조회", description = "담당 환자의 상세 정보를 조회합니다.")
    GlobalApiResponse<PatientDetailRes> getPatientDetail(
            @Parameter(hidden = true) String userUid);

    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음으로 표시합니다.")
    GlobalApiResponse<String> markAlertAsRead(
            @Parameter(hidden = true) String userUid,
            @PathVariable String alertId);
}
