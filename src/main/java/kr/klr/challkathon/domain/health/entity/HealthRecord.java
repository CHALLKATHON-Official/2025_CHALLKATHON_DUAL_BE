package kr.klr.challkathon.domain.health.entity;

import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.global.entity.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.github.f4b6a3.ulid.UlidCreator;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_record", indexes = {
    @Index(name = "idx_health_record_user_date", columnList = "user_uid, record_date")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthRecord extends BaseTime {
    
    @Id
    @Column(name = "record_id", nullable = false, length = 26, unique = true)
    @Builder.Default
    private String recordId = UlidCreator.getUlid().toString();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid", referencedColumnName = "uid")
    private User user;
    
    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "record_time", nullable = false)
    private LocalDateTime recordTime;
    
    // 통증 부위별 점수 (0-3점: 양호,경미,보통,심함)
    @Column(name = "leg_pain_score", nullable = false)
    @Builder.Default
    private Integer legPainScore = 0;
    
    @Column(name = "knee_pain_score", nullable = false)
    @Builder.Default
    private Integer kneePainScore = 0;
    
    @Column(name = "ankle_pain_score", nullable = false)
    @Builder.Default
    private Integer anklePainScore = 0;
    
    @Column(name = "heel_pain_score", nullable = false)
    @Builder.Default
    private Integer heelPainScore = 0;
    
    @Column(name = "back_pain_score", nullable = false)
    @Builder.Default
    private Integer backPainScore = 0;
    
    // 통증 기록 타입 (운동 후 자동 기록인지, 수동 기록인지)
    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", nullable = false)
    @Builder.Default
    private PainRecordType recordType = PainRecordType.MANUAL;
    
    @Column(name = "notes", length = 500)
    private String notes;
    
    @PrePersist
    public void generateRecordId() {
        if (this.recordId == null) {
            this.recordId = UlidCreator.getUlid().toString();
        }
        if (this.recordTime == null) {
            this.recordTime = LocalDateTime.now();
        }
    }

    // 총 통증 점수 계산 (15점 만점)
    public Integer getTotalPainScore() {
        return legPainScore + kneePainScore + anklePainScore + heelPainScore + backPainScore;
    }

    public enum PainRecordType {
        POST_EXERCISE, // 운동 후 자동 기록
        MANUAL // 수동 기록
    }
}
