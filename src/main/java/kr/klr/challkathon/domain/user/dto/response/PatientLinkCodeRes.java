package kr.klr.challkathon.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientLinkCodeRes {
    
    private String linkCode; // 6자리 연동 코드
    private LocalDateTime expiresAt; // 만료 시간 (24시간 후)
    private String message;
}
