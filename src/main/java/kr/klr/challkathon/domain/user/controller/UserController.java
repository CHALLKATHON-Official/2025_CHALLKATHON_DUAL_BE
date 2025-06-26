package kr.klr.challkathon.domain.user.controller;

import kr.klr.challkathon.domain.user.dto.request.PatientProfileSetupReq;
import kr.klr.challkathon.domain.user.dto.request.UpdateUserInfoReq;
import kr.klr.challkathon.domain.user.dto.response.PatientLinkCodeRes;
import kr.klr.challkathon.domain.user.dto.response.UserInfoRes;
import kr.klr.challkathon.domain.user.dto.response.UserProfileStatusRes;
import kr.klr.challkathon.domain.user.service.UserService;
import kr.klr.challkathon.domain.user.spec.UserControllerSpec;
import kr.klr.challkathon.global.customAnnotation.CurrentUser;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController implements UserControllerSpec {

    private final UserService userService;

    @GetMapping("/info")
    @Override
    public GlobalApiResponse<UserInfoRes> getUserInfo(@CurrentUser String userUid) {
        UserInfoRes response = userService.getUserInfo(userUid);
        return GlobalApiResponse.success(response);
    }

    @GetMapping("/profile-status")
    @Override
    public GlobalApiResponse<UserProfileStatusRes> getProfileStatus(@CurrentUser String userUid) {
        UserProfileStatusRes response = userService.getUserProfileStatus(userUid);
        return GlobalApiResponse.success(response);
    }

    @PostMapping("/patient-profile")
    @Override
    public GlobalApiResponse<String> setupPatientProfile(
            @CurrentUser String userUid,
            @Valid @RequestBody PatientProfileSetupReq request) {
        userService.setupPatientProfile(userUid, request);
        return GlobalApiResponse.success("환자 프로필이 설정되었습니다.");
    }

    @PutMapping("/info")
    @Override
    public GlobalApiResponse<String> updateUserInfo(
            @CurrentUser String userUid,
            @Valid @RequestBody UpdateUserInfoReq request) {
        userService.updateUserInfo(userUid, request);
        return GlobalApiResponse.success("사용자 정보가 수정되었습니다.");
    }

    @PutMapping("/patient-info")
    @Override
    public GlobalApiResponse<String> updatePatientInfo(
            @CurrentUser String userUid,
            @Valid @RequestBody PatientProfileSetupReq request) {
        userService.updatePatientInfo(userUid, request);
        return GlobalApiResponse.success("환자 정보가 수정되었습니다.");
    }

    @PostMapping("/patient/link-code")
    @Override
    public GlobalApiResponse<PatientLinkCodeRes> generateLinkCode(@CurrentUser String userUid) {
        PatientLinkCodeRes response = userService.generatePatientLinkCode(userUid);
        return GlobalApiResponse.success(response);
    }

    @DeleteMapping
    @Override
    public GlobalApiResponse<String> deleteUser(@CurrentUser String userUid) {
        userService.deleteUser(userUid);
        return GlobalApiResponse.success("사용자 계정이 삭제되었습니다.");
    }
}
