package kr.klr.challkathon.domain.exercise.entity;

import kr.klr.challkathon.global.entity.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "exercise")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exercise extends BaseTime {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ExerciseCategory category;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ExerciseType type;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @Column(name = "calories_per_minute")
    private Double caloriesPerMinute;
    
    @Column(name = "is_required", nullable = false)
    @Builder.Default
    private Boolean isRequired = false;
    
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @Column(name = "order_index")
    private Integer orderIndex;
}
