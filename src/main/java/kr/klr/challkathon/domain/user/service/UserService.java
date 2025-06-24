package kr.klr.challkathon.domain.user.service;

import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.domain.user.repository.UserRepository;
import kr.klr.challkathon.domain.user.dto.request.UpdateUserInfoReq;
import kr.klr.challkathon.global.globalResponse.error.ErrorCode;
import kr.klr.challkathon.global.globalResponse.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;

    /**
     * username으로 삭제되지 않은 사용자 조회 (없으면 null 반환)
     */
    public User findByUsernameAndIsDeletedFalseOrGetNull(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElse(null);
    }

    /**
     * uid로 사용자 조회
     */
    public User findByUid(String uid) {
        return userRepository.findByUid(uid)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 새 사용자 생성 (OAuth2 전용)
     */
    @Transactional
    public User createUser(String uid, String username, String email, String provider) {
        User user = User.builder()
                .uid(uid)
                .username(username)
                .email(email)
                .provider(provider)
                .role("ROLE_USER")
                .isDeleted(false)
                .build();
        
        return userRepository.save(user);
    }

    /**
     * 사용자 정보 업데이트 (닉네임, 프로필 이미지)
     */
    @Transactional
    public void updateUserInfo(String uid, UpdateUserInfoReq req) {
        User user = findByUid(uid);
        
        if (req.getNickname() != null) {
            user.setNickname(req.getNickname());
        }
        
        if (req.getProfileImage() != null) {
            user.setProfileImage(req.getProfileImage());
        }
        
        userRepository.save(user);
    }

    /**
     * 사용자 삭제 (soft delete)
     */
    @Transactional
    public void deleteUser(String uid) {
        User user = findByUid(uid);
        user.setIsDeleted(true);
        userRepository.save(user);
    }
}
