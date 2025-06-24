package kr.klr.challkathon.global.globalResponse.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
	BAD_REQUEST("400", "잘못된 요청입니다."),
	NOT_FOUND("404", "요청을 찾을 수 없습니다."),
	UNAUTHORIZED("401", "인증되지 않은 사용자입니다."),
	FORBIDDEN("403", "권한이 없습니다."),
	CONFLICT("409", "충돌이 발생했습니다."),
	INTERNAL_SERVER_ERROR("500", "Internal Server Error"),
	SERVICE_UNAVAILABLE("503", "서비스 이용이 불가능합니다."),
	VALIDATION_ERROR("1001", "Validation Error"),
	
	// User 관련 에러
	USER_NOT_FOUND("2001", "사용자를 찾을 수 없습니다."),
	USER_ALREADY_EXISTS("2002", "이미 존재하는 사용자입니다."),
	
	// OAuth 관련 에러
	OAUTH_PROVIDER_NOT_SUPPORTED("3001", "지원하지 않는 OAuth 제공자입니다."),
	OAUTH_ACCESS_TOKEN_FAILED("3002", "OAuth 액세스 토큰 발급에 실패했습니다."),
	OAUTH_USER_INFO_FAILED("3003", "OAuth 사용자 정보 조회에 실패했습니다."),
	
	// JWT 관련 에러
	JWT_TOKEN_INVALID("4001", "유효하지 않은 JWT 토큰입니다."),
	JWT_TOKEN_EXPIRED("4002", "만료된 JWT 토큰입니다.");

	private final String code;
	private final String defaultMessage;

	ErrorCode(String code, String defaultMessage) {
		this.code = code;
		this.defaultMessage = defaultMessage;
	}
}