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

@Entity
@Table(name = "health_record")
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
    
    // 통증 부위별 점수 (1-10점)
    @Column(name = "chest_pain_score")
    private Integer chestPainScore;
    
    @Column(name = "back_pain_score")
    private Integer backPainScore;
    
    @Column(name = "waist_pain_score")
    private Integer waistPainScore;
    
    @Column(name = "neck_pain_score")
    private Integer neckPainScore;
    
    @Column(name = "leg_pain_score")
    private Integer legPainScore;
    
    // 전체 컨디션
    @Enumerated(EnumType.STRING)
    @Column(name = "overall_condition")
    private OverallCondition overallCondition;
    
    @Column(name = "notes", length = 500)
    private String notes;
    
    @PrePersist
    public void generateRecordId() {
        if (this.recordId == null) {
            this.recordId = UlidCreator.getUlid().toString();
        }
    }
}
