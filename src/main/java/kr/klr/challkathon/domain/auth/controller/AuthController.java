package kr.klr.challkathon.domain.auth.controller;

import java.io.Serializable;
import java.util.Map;
import kr.klr.challkathon.domain.auth.dto.request.OAuth2AppReq;
import kr.klr.challkathon.domain.auth.manager.AppOAuth2Manager;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import kr.klr.challkathon.domain.auth.spec.AuthControllerSpec;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerSpec {
    
    private final AppOAuth2Manager appOAuth2Manager;

    /**
     * OAuth2 콜백 처리 (Spring Security OAuth2 Client 사용)
     */
    @PostMapping("/oauth/callback")
    public GlobalApiResponse<Map<String, String>> oauthCallback(@RequestBody OAuth2AppReq req) {
        log.info("OAuth 콜백 요청: provider={}", req.getProvider());
        
        String token = appOAuth2Manager.oauthCallback(req);
        
        return GlobalApiResponse.success(
            Map.of(
                "token", token,
                "message", "로그인 성공"
            )
        );
    }

    /**
     * OAuth2 제공자별 로그인 URL 제공 (OAuth2 Client 기반)
     */
    @GetMapping("/oauth/{provider}/url")
    public GlobalApiResponse<Map<String, String>> getOAuthUrl(@PathVariable String provider) {
        log.info("OAuth URL 요청: provider={}", provider);
        String authUrl = appOAuth2Manager.getAuthorizationUrl(provider);
        return GlobalApiResponse.success(
            Map.of(
                "authUrl", authUrl,
                "provider", provider
            )
        );
    }

    /**
     * 지원하는 OAuth 제공자 목록
     */
    @GetMapping("/oauth/providers")
    public GlobalApiResponse<Map<String, Serializable>> getSupportedProviders() {
        String[] providers = appOAuth2Manager.getSupportedProviders();
        
        return GlobalApiResponse.success(
            Map.of(
                "providers", providers,
                "message", "지원하는 OAuth 제공자 목록"
            )
        );
    }
}
