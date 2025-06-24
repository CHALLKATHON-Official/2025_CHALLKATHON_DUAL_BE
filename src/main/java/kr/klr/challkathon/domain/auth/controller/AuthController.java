package kr.klr.challkathon.domain.auth.controller;

import java.util.Map;
import kr.klr.challkathon.domain.auth.dto.request.OAuth2AppReq;
import kr.klr.challkathon.domain.auth.manager.AppOAuth2Manager;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AppOAuth2Manager appOAuth2Manager;

    /**
     * OAuth2 콜백 처리 (Spring Security OAuth2 Client 사용)
     */
    @PostMapping("/oauth/callback")
    public ResponseEntity<?> oauthCallback(@RequestBody OAuth2AppReq req) {
        log.info("OAuth 콜백 요청: provider={}", req.getProvider());
        
        String token = appOAuth2Manager.oauthCallback(req);
        
        return ResponseEntity.ok(GlobalApiResponse.success(
            Map.of(
                "token", token,
                "message", "로그인 성공"
            )
        ));
    }

    /**
     * OAuth2 제공자별 로그인 URL 제공 (OAuth2 Client 기반)
     */
    @GetMapping("/oauth/{provider}/url")
    public ResponseEntity<?> getOAuthUrl(@PathVariable String provider) {
        log.info("OAuth URL 요청: provider={}", provider);
        
        try {
            String authUrl = appOAuth2Manager.getAuthorizationUrl(provider);
            
            return ResponseEntity.ok(GlobalApiResponse.success(
                Map.of(
                    "authUrl", authUrl,
                    "provider", provider
                )
            ));
        } catch (Exception e) {
            log.error("OAuth URL 생성 실패: provider={}, error={}", provider, e.getMessage());
            return ResponseEntity.badRequest()
                .body(GlobalApiResponse.error("지원하지 않는 OAuth 제공자입니다: " + provider));
        }
    }

    /**
     * 지원하는 OAuth 제공자 목록
     */
    @GetMapping("/oauth/providers")
    public ResponseEntity<?> getSupportedProviders() {
        String[] providers = appOAuth2Manager.getSupportedProviders();
        
        return ResponseEntity.ok(GlobalApiResponse.success(
            Map.of(
                "providers", providers,
                "message", "지원하는 OAuth 제공자 목록"
            )
        ));
    }
}
