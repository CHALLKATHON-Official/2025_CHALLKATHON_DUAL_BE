package kr.klr.challkathon.global.globalResponse.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import kr.klr.challkathon.global.globalResponse.global.GlobalApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<GlobalApiResponse<String>> handleGlobalException(GlobalException e) {
		ErrorCode errorCode = e.getErrorCode();
		log.error("GlobalException 에러 발생: "+ errorCode.name()+ ", "+e.getMessage());
		return new ResponseEntity<>(
			GlobalApiResponse.error(null, e.getMessage(), errorCode.name(), "SHOW_SNACKBAR"),
			HttpStatus.valueOf(errorCode.name()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<GlobalApiResponse<String>> handleException(Exception e) {
		log.error("에러 발생: ", e);
		return new ResponseEntity<>(
			GlobalApiResponse.error(
				ErrorCode.INTERNAL_SERVER_ERROR.getDefaultMessage(), 
				ErrorCode.INTERNAL_SERVER_ERROR.name(),
				"SHOW_SNACKBAR"
			),
			HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<GlobalApiResponse<String>> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error("NoResourceFoundException 에러 발생: {}", e.getMessage());
		return new ResponseEntity<>(
			GlobalApiResponse.error(
				ErrorCode.NOT_FOUND.getDefaultMessage(),
				ErrorCode.NOT_FOUND.name(),
				"SHOW_SNACKBAR"
			),
			HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<GlobalApiResponse<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.error("HttpMessageNotReadableException 에러 발생: {}", e.getMessage());
		return new ResponseEntity<>(
			GlobalApiResponse.error(
				ErrorCode.BAD_REQUEST.getDefaultMessage(),
				ErrorCode.BAD_REQUEST.name(),
				"SHOW_SNACKBAR"
			),
			HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<GlobalApiResponse<String>> handleInvalidFormatException(InvalidFormatException e) {
		log.error("InvalidFormatException 에러 발생: {}", e.getMessage());
		return new ResponseEntity<>(
			GlobalApiResponse.error(
				"요청 데이터 형식이 잘못되었습니다.",
				ErrorCode.BAD_REQUEST.name(),
				"SHOW_SNACKBAR"
			),
			HttpStatus.BAD_REQUEST);
	}
}
