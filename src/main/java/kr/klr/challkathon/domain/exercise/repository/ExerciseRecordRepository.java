package kr.klr.challkathon.domain.exercise.repository;

import kr.klr.challkathon.domain.exercise.entity.ExerciseRecord;
import kr.klr.challkathon.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRecordRepository extends JpaRepository<ExerciseRecord, String> {
    
    List<ExerciseRecord> findByUserAndExerciseDate(User user, LocalDate exerciseDate);
    
    List<ExerciseRecord> findByUserAndExerciseDateOrderByCreatedAtDesc(User user, LocalDate exerciseDate);
    
    List<ExerciseRecord> findByUserAndExerciseDateBetweenOrderByExerciseDateDesc(
            User user, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT er FROM ExerciseRecord er WHERE er.user = :user AND er.exerciseDate = :date AND er.exercise.id = :exerciseId")
    Optional<ExerciseRecord> findByUserAndDateAndExercise(
            @Param("user") User user, 
            @Param("date") LocalDate date, 
            @Param("exerciseId") Long exerciseId);
    
    @Query("SELECT SUM(er.durationMinutes) FROM ExerciseRecord er WHERE er.user = :user AND er.exerciseDate = :date")
    Optional<Integer> getTotalDurationByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);
    
    @Query("SELECT SUM(er.caloriesBurned) FROM ExerciseRecord er WHERE er.user = :user AND er.exerciseDate = :date")
    Optional<Double> getTotalCaloriesByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);
    
    @Query("SELECT COUNT(DISTINCT er.exercise.id) FROM ExerciseRecord er WHERE er.user = :user AND er.exerciseDate = :date AND er.exercise.isRequired = true")
    Long getCompletedRequiredExerciseCount(@Param("user") User user, @Param("date") LocalDate date);
    
    @Query("SELECT MAX(er.distanceKm) FROM ExerciseRecord er WHERE er.user = :user AND er.distanceKm IS NOT NULL")
    Optional<Double> getMaxDistanceByUser(@Param("user") User user);
}
