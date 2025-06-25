package kr.klr.challkathon.domain.auth.manager;

import kr.klr.challkathon.domain.auth.dto.request.OAuth2AppReq;
import kr.klr.challkathon.domain.auth.service.OAuth2Service;
import kr.klr.challkathon.domain.auth.service.OAuth2TokenService;
import kr.klr.challkathon.domain.auth.exception.OAuth2Exception;
import kr.klr.challkathon.global.globalResponse.error.ErrorCode;
import kr.klr.challkathon.global.globalResponse.error.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppOAuth2Manager {
    
    private final OAuth2TokenService tokenService;
    private final OAuth2Service oAuth2Service;
    private final ClientRegistrationRepository clientRegistrationRepository;

    /**
     * OAuth2 콜백 처리 - Spring Security OAuth2 Client 사용
     */
    public String oauthCallback(OAuth2AppReq req) {
        try {
            log.info("OAuth 콜백 처리 시작: provider={}", req.getProvider());
            
            // ClientRegistration에서 리다이렉트 URI 가져오기
            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(req.getProvider());
            if (clientRegistration == null) {
                throw OAuth2Exception.providerNotSupported(req.getProvider());
            }
            
            String redirectUri = clientRegistration.getRedirectUri();
            
            // Authorization Code → Access Token (OAuth2 Client 사용)
            OAuth2AccessToken accessToken = tokenService.exchangeCodeForToken(
                    req.getProvider(), req.getCode(), redirectUri);
            
            // Access Token → JWT Token
            String jwtToken = oAuth2Service.processOAuth2User(req.getProvider(), accessToken);
            
            log.info("OAuth 로그인 성공: provider={}", req.getProvider());
            return jwtToken;
            
        } catch (OAuth2Exception e) {
            log.error("OAuth 처리 실패: provider={}, error={}", req.getProvider(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("OAuth 처리 중 예상치 못한 오류: provider={}, error={}", req.getProvider(), e.getMessage(), e);
            throw OAuth2Exception.accessTokenFailed(req.getProvider(), e.getMessage());
        }
    }

    /**
     * 지원하는 OAuth 제공자 목록
     */
    public String[] getSupportedProviders() {
        return oAuth2Service.getSupportedProviders();
    }

    /**
     * OAuth 인증 URL 생성
     */
    public String getAuthorizationUrl(String provider) {
        try {
            return oAuth2Service.getAuthorizationUrl(provider);
        } catch (Error e) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "지원하지 않는 OAuth 제공자입니다: " + provider);
        }
    }
}
