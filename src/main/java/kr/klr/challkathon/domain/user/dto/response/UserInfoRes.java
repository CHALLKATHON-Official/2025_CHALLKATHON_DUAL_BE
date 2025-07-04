package kr.klr.challkathon.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRes {
    
    private String uid;
    private String username;
    private String nickname;
    private String email;
    private String profileImage;
}
