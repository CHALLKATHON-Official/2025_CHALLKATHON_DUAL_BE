package kr.klr.challkathon.domain.auth.service;

import kr.klr.challkathon.domain.auth.exception.OAuth2Exception;
import kr.klr.challkathon.global.globalResponse.error.ErrorCode;
import kr.klr.challkathon.global.globalResponse.error.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2TokenService {
    
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final RestTemplate restTemplate;

    public OAuth2AccessToken exchangeCodeForToken(String registrationId, String code, String redirectUri) {
        try {
            // ClientRegistration 조회
            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
            if (clientRegistration == null) {
                log.error("지원하지 않는 OAuth registrationId: {}", registrationId);
                throw new GlobalException(ErrorCode.BAD_REQUEST, "지원하지 않는 OAuth 제공자입니다: " + registrationId);
            }

            // 토큰 요청 파라미터 생성
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", clientRegistration.getClientId());
            params.add("client_secret", clientRegistration.getClientSecret());
            params.add("redirect_uri", redirectUri);
            params.add("code", code);

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // HTTP 요청 생성
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            // 토큰 요청 실행
            String tokenUri = clientRegistration.getProviderDetails().getTokenUri();
            Map<String, Object> response = restTemplate.postForObject(tokenUri, request, Map.class);

            if (response == null || !response.containsKey("access_token")) {
                log.error("OAuth2 토큰 응답이 올바르지 않음: {}", response);
                throw new GlobalException(ErrorCode.OAUTH_ACCESS_TOKEN_FAILED, "토큰 응답이 올바르지 않습니다");
            }

            // OAuth2AccessToken 생성
            String accessTokenValue = response.get("access_token").toString();
            OAuth2AccessToken.TokenType tokenType = OAuth2AccessToken.TokenType.BEARER;
            
            // 만료 시간 처리 (선택적)
            Long expiresIn = response.containsKey("expires_in") ? 
                Long.parseLong(response.get("expires_in").toString()) : null;
            
            OAuth2AccessToken accessToken = new OAuth2AccessToken(
                tokenType, 
                accessTokenValue, 
                null, // issuedAt
                expiresIn != null ? java.time.Instant.now().plusSeconds(expiresIn) : null // expiresAt
            );
            
            log.info("OAuth2 Access Token 발급 성공: registrationId={}, tokenType={}", 
                    registrationId, tokenType.getValue());
            
            return accessToken;
        } catch (Exception e) {
            log.error("OAuth2 Access Token 교환 실패: registrationId={}, error={}", registrationId, e.getMessage());
            throw new GlobalException(ErrorCode.OAUTH_ACCESS_TOKEN_FAILED, e.getMessage());
        }
    }
}
