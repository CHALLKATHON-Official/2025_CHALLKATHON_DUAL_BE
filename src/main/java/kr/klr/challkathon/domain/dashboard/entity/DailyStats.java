package kr.klr.challkathon.domain.dashboard.entity;

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
@Table(name = "daily_stats", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_uid", "stats_date"}),
       indexes = {
           @Index(name = "idx_daily_stats_user_date", columnList = "user_uid, stats_date")
       })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyStats extends BaseTime {
    
    @Id
    @Column(name = "stats_id", nullable = false, length = 26, unique = true)
    @Builder.Default
    private String statsId = UlidCreator.getUlid().toString();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid", referencedColumnName = "uid")
    private User user;
    
    @Column(name = "stats_date", nullable = false)
    private LocalDate statsDate;
    
    @Column(name = "total_steps", nullable = false)
    @Builder.Default
    private Integer totalSteps = 0;
    
    @Column(name = "total_exercise_minutes", nullable = false)
    @Builder.Default
    private Integer totalExerciseMinutes = 0;
    
    @Column(name = "total_distance_km", nullable = false)
    @Builder.Default
    private Double totalDistanceKm = 0.0;
    
    @Column(name = "required_exercises_completed", nullable = false)
    @Builder.Default
    private Integer requiredExercisesCompleted = 0;
    
    @Column(name = "total_exercises_completed", nullable = false)
    @Builder.Default
    private Integer totalExercisesCompleted = 0;
    
    @Column(name = "today_pain_level")
    private Integer todayPainLevel; // 오늘의 통증 수준 (0-15점)
    
    @PrePersist
    public void generateStatsId() {
        if (this.statsId == null) {
            this.statsId = UlidCreator.getUlid().toString();
        }
    }
}
