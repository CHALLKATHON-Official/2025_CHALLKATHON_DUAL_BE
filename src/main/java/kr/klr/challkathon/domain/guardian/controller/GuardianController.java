package kr.klr.challkathon.domain.guardian.controller;

import kr.klr.challkathon.domain.guardian.dto.request.PatientLinkReq;
import kr.klr.challkathon.domain.guardian.dto.response.GuardianDashboardRes;
import kr.klr.challkathon.domain.guardian.dto.response.PatientDetailRes;
import kr.klr.challkathon.domain.guardian.service.GuardianService;
import kr.klr.challkathon.domain.guardian.spec.GuardianControllerSpec;
import kr.klr.challkathon.domain.user.service.UserService;
import kr.klr.challkathon.global.customAnnotation.CurrentUser;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/guardian")
@RequiredArgsConstructor
public class GuardianController implements GuardianControllerSpec {

    private final GuardianService guardianService;
    private final UserService userService;

    @PostMapping("/link-patient")
    @Override
    public GlobalApiResponse<String> linkPatient(
            @CurrentUser String userUid,
            @Valid @RequestBody PatientLinkReq request) {
        userService.linkPatientToGuardian(userUid, request.getPatientLinkCode());
        return GlobalApiResponse.success("환자와 성공적으로 연동되었습니다. 보호자 프로필이 생성되었습니다.");
    }

    @GetMapping("/dashboard")
    @Override
    public GlobalApiResponse<GuardianDashboardRes> getGuardianDashboard(@CurrentUser String userUid) {
        GuardianDashboardRes response = guardianService.getGuardianDashboard(userUid);
        return GlobalApiResponse.success(response);
    }

    @GetMapping("/patient-detail")
    @Override
    public GlobalApiResponse<PatientDetailRes> getPatientDetail(@CurrentUser String userUid) {
        PatientDetailRes response = guardianService.getPatientDetail(userUid);
        return GlobalApiResponse.success(response);
    }

    @PutMapping("/alert/{alertId}/read")
    @Override
    public GlobalApiResponse<String> markAlertAsRead(
            @CurrentUser String userUid,
            @PathVariable String alertId) {
        guardianService.markAlertAsRead(userUid, alertId);
        return GlobalApiResponse.success("알림이 읽음 처리되었습니다.");
    }
}
