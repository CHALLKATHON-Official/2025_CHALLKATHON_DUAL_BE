package kr.klr.challkathon.domain.user.controller;

import kr.klr.challkathon.domain.user.dto.request.UpdateUserInfoReq;
import kr.klr.challkathon.domain.user.dto.response.UserInfoRes;
import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.domain.user.service.UserService;
import kr.klr.challkathon.global.customAnnotation.CurrentUser;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import kr.klr.challkathon.domain.user.spec.UserControllerSpec;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController implements UserControllerSpec {

    private final UserService userService;

    @GetMapping("/me")
    public GlobalApiResponse<UserInfoRes> getCurrentUser(@CurrentUser String userUid) {
        log.info("사용자 정보 조회 요청: uid={}", userUid);
        
        User user = userService.findByUid(userUid);
        UserInfoRes userInfo = UserInfoRes.from(user);
        
        return GlobalApiResponse.success(userInfo);
    }

    @PutMapping("/me")
    public GlobalApiResponse<UserInfoRes> updateUserInfo(
            @CurrentUser String userUid,
            @RequestBody UpdateUserInfoReq req) {
        
        log.info("사용자 정보 업데이트 요청: uid={}", userUid);
        
        userService.updateUserInfo(userUid, req);
        User updatedUser = userService.findByUid(userUid);
        UserInfoRes userInfo = UserInfoRes.from(updatedUser);
        
        return GlobalApiResponse.success(userInfo);
    }

    @DeleteMapping("/me")
    public GlobalApiResponse<?> deleteUser(@CurrentUser String userUid) {
        log.info("사용자 탈퇴 요청: uid={}", userUid);
        
        userService.deleteUser(userUid);
        
        return GlobalApiResponse.success("회원 탈퇴가 완료되었습니다.");
    }
}
