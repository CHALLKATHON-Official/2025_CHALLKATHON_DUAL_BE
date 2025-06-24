package kr.klr.challkathon.global.globalResponse.error;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
	private final ErrorCode errorCode;

	public GlobalException(ErrorCode errorCode) {
		super(errorCode.getDefaultMessage());
		this.errorCode = errorCode;
	}

	public GlobalException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}