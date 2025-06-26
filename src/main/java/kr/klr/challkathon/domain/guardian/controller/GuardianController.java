package kr.klr.challkathon.domain.guardian.controller;

import kr.klr.challkathon.domain.guardian.dto.request.PatientLinkReq;
import kr.klr.challkathon.domain.guardian.dto.response.GuardianDashboardRes;
import kr.klr.challkathon.domain.guardian.dto.response.PatientDetailRes;
import kr.klr.challkathon.domain.guardian.service.GuardianService;
import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.domain.user.service.UserService;
import kr.klr.challkathon.global.customAnnotation.CurrentUser;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/guardian")
@RequiredArgsConstructor
@Tag(name = "Guardian", description = "보호자 관련 API")
public class GuardianController {

    private final GuardianService guardianService;
    private final UserService userService;

    @PostMapping("/link-patient")
    @Operation(summary = "환자 연동", description = "환자가 제공한 6자리 연동 코드로 환자와 연동하여 보호자 프로필을 생성합니다.")
    public GlobalApiResponse<String> linkPatient(
            @CurrentUser User guardian,
            @Valid @RequestBody PatientLinkReq request) {
        userService.linkPatientToGuardian(guardian, request.getPatientLinkCode());
        return GlobalApiResponse.success("환자와 성공적으로 연동되었습니다. 보호자 프로필이 생성되었습니다.");
    }

    @GetMapping("/dashboard")
    @Operation(summary = "보호자 메인페이지 조회", description = "보호자 대시보드 정보를 조회합니다.")
    public GlobalApiResponse<GuardianDashboardRes> getGuardianDashboard(@CurrentUser User guardian) {
        GuardianDashboardRes response = guardianService.getGuardianDashboard(guardian);
        return GlobalApiResponse.success(response);
    }

    @GetMapping("/patient-detail")
    @Operation(summary = "환자 정보 페이지 조회", description = "담당 환자의 상세 정보를 조회합니다.")
    public GlobalApiResponse<PatientDetailRes> getPatientDetail(@CurrentUser User guardian) {
        PatientDetailRes response = guardianService.getPatientDetail(guardian);
        return GlobalApiResponse.success(response);
    }

    @PutMapping("/alert/{alertId}/read")
    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음으로 표시합니다.")
    public GlobalApiResponse<String> markAlertAsRead(
            @CurrentUser User guardian,
            @PathVariable String alertId) {
        guardianService.markAlertAsRead(guardian, alertId);
        return GlobalApiResponse.success("알림이 읽음 처리되었습니다.");
    }
}
