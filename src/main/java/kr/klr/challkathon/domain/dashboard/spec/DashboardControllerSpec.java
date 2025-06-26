package kr.klr.challkathon.domain.dashboard.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.klr.challkathon.domain.dashboard.dto.response.PatientDashboardRes;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;

@Tag(name = "Dashboard", description = "대시보드 관련 API")
public interface DashboardControllerSpec {

    @Operation(summary = "환자 메인페이지 조회", description = "환자의 오늘의 요약과 주간 걸음 수를 조회합니다.")
    GlobalApiResponse<PatientDashboardRes> getPatientDashboard(
            @Parameter(hidden = true) String userUid);
}