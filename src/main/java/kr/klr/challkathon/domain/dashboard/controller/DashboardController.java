package kr.klr.challkathon.domain.dashboard.controller;

import kr.klr.challkathon.domain.dashboard.dto.response.PatientDashboardRes;
import kr.klr.challkathon.domain.dashboard.service.DashboardService;
import kr.klr.challkathon.domain.dashboard.spec.DashboardControllerSpec;
import kr.klr.challkathon.global.customAnnotation.CurrentUser;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController implements DashboardControllerSpec {

    private final DashboardService dashboardService;

    @GetMapping("/patient")
    @Override
    public GlobalApiResponse<PatientDashboardRes> getPatientDashboard(@CurrentUser String userUid) {
        PatientDashboardRes response = dashboardService.getPatientDashboard(userUid);
        return GlobalApiResponse.success(response);
    }
}
