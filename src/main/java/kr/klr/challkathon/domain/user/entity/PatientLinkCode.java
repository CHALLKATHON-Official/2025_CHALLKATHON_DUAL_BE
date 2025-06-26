package kr.klr.challkathon.domain.user.entity;

import kr.klr.challkathon.global.entity.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.github.f4b6a3.ulid.UlidCreator;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_link_code", indexes = {
    @Index(name = "idx_patient_link_code", columnList = "link_code"),
    @Index(name = "idx_patient_link_user", columnList = "patient_uid")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientLinkCode extends BaseTime {
    
    @Id
    @Column(name = "id", nullable = false, length = 26, unique = true)
    @Builder.Default
    private String id = UlidCreator.getUlid().toString();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_uid", referencedColumnName = "uid")
    private User patient;
    
    @Column(name = "link_code", nullable = false, length = 6, unique = true)
    private String linkCode; // 6자리 랜덤 코드
    
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt; // 만료 시간
    
    @Column(name = "is_used", nullable = false)
    @Builder.Default
    private Boolean isUsed = false; // 사용 여부
    
    @PrePersist
    public void generateId() {
        if (this.id == null) {
            this.id = UlidCreator.getUlid().toString();
        }
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean isValid() {
        return !isUsed && !isExpired();
    }
}
