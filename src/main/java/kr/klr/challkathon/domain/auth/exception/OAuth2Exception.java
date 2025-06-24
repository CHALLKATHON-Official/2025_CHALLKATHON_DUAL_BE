package kr.klr.challkathon.domain.auth.exception;

import kr.klr.challkathon.global.globalResponse.error.ErrorCode;
import kr.klr.challkathon.global.globalResponse.error.GlobalException;

/**
 * OAuth2 관련 예외 클래스
 */
public class OAuth2Exception extends GlobalException {

    public OAuth2Exception(ErrorCode errorCode) {
        super(errorCode);
    }

    public OAuth2Exception(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public OAuth2Exception(ErrorCode errorCode, Throwable cause) {
        super(errorCode, String.valueOf(cause));
    }

    public static OAuth2Exception providerNotSupported(String provider) {
        return new OAuth2Exception(
            ErrorCode.OAUTH_PROVIDER_NOT_SUPPORTED, 
            "지원하지 않는 OAuth 제공자입니다: " + provider
        );
    }

    public static OAuth2Exception accessTokenFailed(String provider, String message) {
        return new OAuth2Exception(
            ErrorCode.OAUTH_ACCESS_TOKEN_FAILED, 
            String.format("OAuth 액세스 토큰 발급 실패 [%s]: %s", provider, message)
        );
    }

    public static OAuth2Exception userInfoFailed(String provider, String message) {
        return new OAuth2Exception(
            ErrorCode.OAUTH_USER_INFO_FAILED, 
            String.format("OAuth 사용자 정보 조회 실패 [%s]: %s", provider, message)
        );
    }
}
