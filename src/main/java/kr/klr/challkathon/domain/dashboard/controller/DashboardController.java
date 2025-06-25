package kr.klr.challkathon.domain.dashboard.controller;

import kr.klr.challkathon.domain.dashboard.dto.DashboardRes;
import kr.klr.challkathon.domain.dashboard.service.DashboardService;
import kr.klr.challkathon.global.customAnnotation.CurrentUser;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import kr.klr.challkathon.domain.dashboard.spec.DashboardControllerSpec;

@Slf4j
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController implements DashboardControllerSpec {
    
    private final DashboardService dashboardService;
    
    @Operation(summary = "환자 메인 대시보드", description = "환자의 메인화면에 표시될 대시보드 정보를 조회합니다.")
    @GetMapping("/patient")
    public GlobalApiResponse<DashboardRes> getPatientDashboard(
            @CurrentUser String userUid) {
        
        DashboardRes response = dashboardService.getPatientDashboard(userUid);
        
        return GlobalApiResponse.success(response);
    }
}
