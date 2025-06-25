package kr.klr.challkathon.domain.dashboard.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Dashboard", description = "대시보드 관련 API")
public interface DashboardControllerSpec {

    @Operation(summary = "환자 메인 대시보드", description = "환자의 메인화면에 표시될 대시보드 정보를 조회합니다.")
    @GetMapping("/api/v1/dashboard/patient")
    Object getPatientDashboard(String userUid);
} 