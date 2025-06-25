package kr.klr.challkathon.domain.user.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.klr.challkathon.domain.user.dto.request.UpdateUserInfoReq;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관련 API")
public interface UserControllerSpec {

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/api/v1/user/me")
    GlobalApiResponse<?> getCurrentUser(
            @Parameter(hidden = true) String userUid);

    @Operation(summary = "내 정보 수정", description = "현재 로그인한 사용자의 정보를 수정합니다.")
    @PutMapping("/api/v1/user/me")
    GlobalApiResponse<?> updateUserInfo(
            @Parameter(hidden = true) String userUid,
            UpdateUserInfoReq req);

    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자를 탈퇴 처리합니다.")
    @DeleteMapping("/api/v1/user/me")
    GlobalApiResponse<?> deleteUser(
            @Parameter(hidden = true) String userUid);
} 