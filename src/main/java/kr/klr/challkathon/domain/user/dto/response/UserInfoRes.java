package kr.klr.challkathon.domain.user.dto.response;

import kr.klr.challkathon.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRes {
    
    private String uid;
    private String username;
    private String nickname;
    private String profileImage;
    private String provider;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserInfoRes from(User user) {
        return UserInfoRes.builder()
                .uid(user.getUid())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .provider(user.getProvider())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
