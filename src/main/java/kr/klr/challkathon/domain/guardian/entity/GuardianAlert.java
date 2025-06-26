package kr.klr.challkathon.domain.guardian.entity;

import kr.klr.challkathon.domain.user.entity.User;
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
@Table(name = "guardian_alert", indexes = {
    @Index(name = "idx_guardian_alert_guardian", columnList = "guardian_uid"),
    @Index(name = "idx_guardian_alert_patient", columnList = "patient_uid")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardianAlert extends BaseTime {
    
    @Id
    @Column(name = "alert_id", nullable = false, length = 26, unique = true)
    @Builder.Default
    private String alertId = UlidCreator.getUlid().toString();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guardian_uid", referencedColumnName = "uid")
    private User guardian;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_uid", referencedColumnName = "uid")
    private User patient;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    private AlertType alertType;
    
    @Column(name = "alert_time", nullable = false)
    private LocalDateTime alertTime;
    
    @Column(name = "previous_pain_level")
    private Integer previousPainLevel;
    
    @Column(name = "current_pain_level")
    private Integer currentPainLevel;
    
    @Column(name = "message", length = 500)
    private String message;
    
    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;
    
    @PrePersist
    public void generateAlertId() {
        if (this.alertId == null) {
            this.alertId = UlidCreator.getUlid().toString();
        }
        if (this.alertTime == null) {
            this.alertTime = LocalDateTime.now();
        }
    }

    public enum AlertType {
        PAIN_INCREASE, // 통증 증가
        EMERGENCY      // 응급상황
    }
}
