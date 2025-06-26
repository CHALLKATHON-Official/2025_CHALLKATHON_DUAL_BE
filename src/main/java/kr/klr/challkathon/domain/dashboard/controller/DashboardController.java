package kr.klr.challkathon.domain.dashboard.controller;

import kr.klr.challkathon.domain.dashboard.dto.response.PatientDashboardRes;
import kr.klr.challkathon.domain.dashboard.service.DashboardService;
import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.global.customAnnotation.CurrentUser;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "대시보드 API")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/patient")
    @Operation(summary = "환자 메인페이지 조회", description = "환자의 오늘의 요약과 주간 걸음 수를 조회합니다.")
    public GlobalApiResponse<PatientDashboardRes> getPatientDashboard(@CurrentUser User user) {
        PatientDashboardRes response = dashboardService.getPatientDashboard(user);
        return GlobalApiResponse.success(response);
    }
}
