package kr.klr.challkathon.domain.user.controller;

import kr.klr.challkathon.domain.user.dto.request.PatientProfileSetupReq;
import kr.klr.challkathon.domain.user.dto.request.UpdateUserInfoReq;
import kr.klr.challkathon.domain.user.dto.response.PatientLinkCodeRes;
import kr.klr.challkathon.domain.user.dto.response.UserInfoRes;
import kr.klr.challkathon.domain.user.dto.response.UserProfileStatusRes;
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
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    @Operation(summary = "사용자 정보 조회", description = "현재 로그인한 사용자의 기본 정보를 조회합니다.")
    public GlobalApiResponse<UserInfoRes> getUserInfo(@CurrentUser User user) {
        UserInfoRes response = UserInfoRes.builder()
                .uid(user.getUid())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .build();
        
        return GlobalApiResponse.success(response);
    }

    @GetMapping("/profile-status")
    @Operation(summary = "사용자 프로필 상태 조회", description = "환자/보호자 프로필 설정 상태를 조회합니다.")
    public GlobalApiResponse<UserProfileStatusRes> getProfileStatus(@CurrentUser User user) {
        UserProfileStatusRes response = userService.getUserProfileStatus(user);
        return GlobalApiResponse.success(response);
    }

    @PostMapping("/patient-profile")
    @Operation(summary = "환자 프로필 설정", description = "환자로서 필요한 정보를 설정합니다.")
    public GlobalApiResponse<String> setupPatientProfile(
            @CurrentUser User user,
            @Valid @RequestBody PatientProfileSetupReq request) {
        userService.setupPatientProfile(user, request);
        return GlobalApiResponse.success("환자 프로필이 설정되었습니다.");
    }

    @PutMapping("/info")
    @Operation(summary = "사용자 기본 정보 수정", description = "닉네임, 프로필 이미지를 수정합니다.")
    public GlobalApiResponse<String> updateUserInfo(
            @CurrentUser User user,
            @Valid @RequestBody UpdateUserInfoReq request) {
        userService.updateUserInfo(user.getUid(), request);
        return GlobalApiResponse.success("사용자 정보가 수정되었습니다.");
    }

    @PutMapping("/patient-info")
    @Operation(summary = "환자 정보 수정", description = "환자 정보를 수정합니다.")
    public GlobalApiResponse<String> updatePatientInfo(
            @CurrentUser User user,
            @Valid @RequestBody PatientProfileSetupReq request) {
        userService.updatePatientInfo(user, request);
        return GlobalApiResponse.success("환자 정보가 수정되었습니다.");
    }

    @PostMapping("/patient/link-code")
    @Operation(summary = "환자 연동 코드 생성", description = "보호자가 사용할 6자리 연동 코드를 생성합니다. (환자 프로필 필요)")
    public GlobalApiResponse<PatientLinkCodeRes> generateLinkCode(@CurrentUser User user) {
        PatientLinkCodeRes response = userService.generatePatientLinkCode(user);
        return GlobalApiResponse.success(response);
    }

    @DeleteMapping
    @Operation(summary = "사용자 삭제", description = "사용자 계정을 삭제합니다.")
    public GlobalApiResponse<String> deleteUser(@CurrentUser User user) {
        userService.deleteUser(user.getUid());
        return GlobalApiResponse.success("사용자 계정이 삭제되었습니다.");
    }
}
