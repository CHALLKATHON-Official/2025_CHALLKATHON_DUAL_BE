package kr.klr.challkathon.domain.exercise.repository;

import kr.klr.challkathon.domain.exercise.entity.Exercise;
import kr.klr.challkathon.domain.exercise.entity.ExerciseCategory;
import kr.klr.challkathon.domain.exercise.entity.ExerciseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    
    List<Exercise> findByTypeAndIsActiveTrueOrderByOrderIndexAsc(ExerciseType type);
    
    List<Exercise> findByTypeAndCategoryAndIsActiveTrueOrderByOrderIndexAsc(
            ExerciseType type, ExerciseCategory category);
    
    @Query("SELECT e FROM Exercise e WHERE e.type = :type AND e.isActive = true ORDER BY e.category, e.orderIndex")
    List<Exercise> findActiveExercisesByTypeGroupedByCategory(@Param("type") ExerciseType type);
    
    List<Exercise> findByIsRequiredTrueAndIsActiveTrue();
    
    List<Exercise> findByIsRequiredFalseAndIsActiveTrue();
    
    @Query("SELECT e FROM Exercise e WHERE e.isActive = true ORDER BY e.isRequired DESC, e.orderIndex ASC")
    List<Exercise> findAllActiveExercisesOrderedByRequiredFirst();
}
