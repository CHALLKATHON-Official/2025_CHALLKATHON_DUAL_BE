package kr.klr.challkathon.domain.auth.provider;

import kr.klr.challkathon.domain.auth.dto.response.OAuth2Res;

public interface OAuthProviderService {
    /** provider 식별자 ("kakao", "google" 등) */
    String getProvider();

    /** Authorization Code → AccessToken 교환 */
    String getAccessToken(String code);

    /** AccessToken → 사용자 정보 조회 → OAuth2Res 반환 */
    OAuth2Res getOAuth2Res(String accessToken);
}