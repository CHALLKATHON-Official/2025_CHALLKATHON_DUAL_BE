package kr.klr.challkathon.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class OAuth2AppReq {
    private String provider; // ex) "kakao", "google"
    private String code;     // 앱에서 발급받은 authorization code
}