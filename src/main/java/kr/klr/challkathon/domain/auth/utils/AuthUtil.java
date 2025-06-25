package kr.klr.challkathon.domain.auth.utils;

import java.util.Arrays;
import java.util.List;
import kr.klr.challkathon.global.globalResponse.error.ErrorCode;
import kr.klr.challkathon.global.globalResponse.error.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final JwtUtil jwtUtil;

    public String getLowerCaseUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "아이디가 없습니다.");
        }
        if (!userId.matches("^[a-zA-Z0-9._-]+$")) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "아이디는 영문자, 숫자, 일부 특수문자(._-)만 사용할 수 있습니다.");
        }
        return userId.toLowerCase();
    }

    /**
     * HttpServletRequest에서 JWT 토큰을 추출하고 사용자 UID를 반환
     */
    public String getCurrentUserUid(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            throw new GlobalException(ErrorCode.JWT_TOKEN_INVALID, "JWT 토큰이 없습니다.");
        }
        
        try {
            return jwtUtil.getUserUid(token);
        } catch (Exception e) {
            log.error("JWT 토큰에서 사용자 UID 추출 실패: {}", e.getMessage());
            throw new GlobalException(ErrorCode.JWT_TOKEN_INVALID, "유효하지 않은 JWT 토큰입니다.");
        }
    }

    /**
     * HttpServletRequest에서 사용자명을 반환
     */
    public String getCurrentUsername(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token == null) {
            throw new GlobalException(ErrorCode.JWT_TOKEN_INVALID, "JWT 토큰이 없습니다.");
        }
        
        try {
            return jwtUtil.getUsername(token);
        } catch (Exception e) {
            log.error("JWT 토큰에서 사용자명 추출 실패: {}", e.getMessage());
            throw new GlobalException(ErrorCode.JWT_TOKEN_INVALID, "유효하지 않은 JWT 토큰입니다.");
        }
    }

    /**
     * HttpServletRequest에서 Bearer 토큰 추출
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거
        }
        
        return null;
    }

    private String generateSecurePassword() {
        // 16자리 랜덤 비밀번호 생성 (대소문자, 숫자, 특수문자 포함)
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < 16; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        
        return password.toString();
    }
}
