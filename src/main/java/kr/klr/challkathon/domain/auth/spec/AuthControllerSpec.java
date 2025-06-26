package kr.klr.challkathon.domain.auth.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.klr.challkathon.domain.auth.dto.request.OAuth2AppReq;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Map;

@Tag(name = "Auth", description = "인증 관련 API")
public interface AuthControllerSpec {

    @Operation(summary = "OAuth2 콜백 처리", description = "OAuth2 인증 후 콜백을 처리합니다.")
    GlobalApiResponse<Map<String, String>> oauthCallback(@RequestBody OAuth2AppReq req);

    @Operation(summary = "OAuth2 제공자별 로그인 URL 제공", description = "OAuth2 제공자별 로그인 URL을 반환합니다.")
    GlobalApiResponse<Map<String, String>> getOAuthUrl(@PathVariable String provider);

    @Operation(summary = "지원하는 OAuth 제공자 목록", description = "지원하는 OAuth 제공자 목록을 반환합니다.")
    GlobalApiResponse<Map<String, Serializable>> getSupportedProviders();
}