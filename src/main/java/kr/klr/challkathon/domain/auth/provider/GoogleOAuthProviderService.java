package kr.klr.challkathon.domain.auth.provider;

/**
 * Google OAuth2 Provider Service
 * 
 * 이 클래스는 더 이상 사용하지 않습니다.
 * Spring Boot OAuth2 Client 자동 설정을 사용하여 OAuth2 인증을 처리합니다.
 * 
 * OAuth2 설정은 application-oauth-google.yml에서 관리됩니다.
 * 실제 OAuth2 로직은 OAuth2Service에서 Spring Security의 DefaultOAuth2UserService를 사용합니다.
 */
@Deprecated
public class GoogleOAuthProviderService {
    // Spring Boot OAuth2 Client 자동 설정 사용으로 더 이상 필요하지 않음
}
