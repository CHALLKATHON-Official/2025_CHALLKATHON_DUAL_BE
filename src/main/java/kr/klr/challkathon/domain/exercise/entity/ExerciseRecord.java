package kr.klr.challkathon.domain.exercise.entity;

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
@Table(name = "exercise_record")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseRecord extends BaseTime {
    
    @Id
    @Column(name = "record_id", nullable = false, length = 26, unique = true)
    @Builder.Default
    private String recordId = UlidCreator.getUlid().toString();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid", referencedColumnName = "uid")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;
    
    @Column(name = "exercise_date", nullable = false)
    private LocalDate exerciseDate;
    
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;
    
    @Column(name = "calories_burned")
    private Double caloriesBurned;
    
    @Column(name = "steps")
    private Integer steps;
    
    @Column(name = "distance_km")
    private Double distanceKm;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "completion_status", nullable = false)
    @Builder.Default
    private CompletionStatus completionStatus = CompletionStatus.COMPLETED;
    
    @Column(name = "notes", length = 500)
    private String notes;
    
    @PrePersist
    public void generateRecordId() {
        if (this.recordId == null) {
            this.recordId = UlidCreator.getUlid().toString();
        }
    }
}
