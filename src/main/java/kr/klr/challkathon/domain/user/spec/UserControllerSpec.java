package kr.klr.challkathon.domain.user.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.klr.challkathon.domain.user.dto.request.PatientProfileSetupReq;
import kr.klr.challkathon.domain.user.dto.request.UpdateUserInfoReq;
import kr.klr.challkathon.domain.user.dto.response.PatientLinkCodeRes;
import kr.klr.challkathon.domain.user.dto.response.UserInfoRes;
import kr.klr.challkathon.domain.user.dto.response.UserProfileStatusRes;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "User", description = "사용자 관련 API")
public interface UserControllerSpec {

    @Operation(summary = "사용자 정보 조회", description = "현재 로그인한 사용자의 기본 정보를 조회합니다.")
    GlobalApiResponse<UserInfoRes> getUserInfo(@Parameter(hidden = true) String userUid);

    @Operation(summary = "사용자 프로필 상태 조회", description = "환자/보호자 프로필 설정 상태를 조회합니다.")
    GlobalApiResponse<UserProfileStatusRes> getProfileStatus(@Parameter(hidden = true) String userUid);

    @Operation(summary = "환자 프로필 설정", description = "환자로서 필요한 정보를 설정합니다.")
    GlobalApiResponse<String> setupPatientProfile(
            @Parameter(hidden = true) String userUid,
            @Valid @RequestBody PatientProfileSetupReq request);

    @Operation(summary = "사용자 기본 정보 수정", description = "닉네임, 프로필 이미지를 수정합니다.")
    GlobalApiResponse<String> updateUserInfo(
            @Parameter(hidden = true) String userUid,
            @Valid @RequestBody UpdateUserInfoReq request);

    @Operation(summary = "환자 정보 수정", description = "환자 정보를 수정합니다.")
    GlobalApiResponse<String> updatePatientInfo(
            @Parameter(hidden = true) String userUid,
            @Valid @RequestBody PatientProfileSetupReq request);

    @Operation(summary = "환자 연동 코드 생성", description = "보호자가 사용할 6자리 연동 코드를 생성합니다. (환자 프로필 필요)")
    GlobalApiResponse<PatientLinkCodeRes> generateLinkCode(@Parameter(hidden = true) String userUid);

    @Operation(summary = "사용자 삭제", description = "사용자 계정을 삭제합니다.")
    GlobalApiResponse<String> deleteUser(@Parameter(hidden = true) String userUid);
}