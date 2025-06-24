package kr.klr.challkathon.domain.auth.utils;

import java.util.Arrays;
import java.util.List;
import kr.klr.challkathon.global.globalResponse.error.ErrorCode;
import kr.klr.challkathon.global.globalResponse.error.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthUtil {

    public String getLowerCaseUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "아이디가 없습니다.");
        }
        if (!userId.matches("^[a-zA-Z0-9._-]+$")) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "아이디는 영문자, 숫자, 일부 특수문자(._-)만 사용할 수 있습니다.");
        }
        return userId.toLowerCase();
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
