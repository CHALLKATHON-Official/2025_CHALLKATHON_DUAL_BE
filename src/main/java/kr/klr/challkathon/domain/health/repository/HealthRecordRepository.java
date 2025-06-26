package kr.klr.challkathon.domain.health.repository;

import kr.klr.challkathon.domain.health.entity.HealthRecord;
import kr.klr.challkathon.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecord, String> {
    
    Optional<HealthRecord> findByUserAndRecordDate(User user, LocalDate recordDate);
    
    List<HealthRecord> findByUserAndRecordDateBetweenOrderByRecordDateDesc(
            User user, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT hr FROM HealthRecord hr WHERE hr.user = :user ORDER BY hr.recordDate DESC LIMIT 7")
    List<HealthRecord> findRecentRecordsByUser(@Param("user") User user);
    
    Optional<HealthRecord> findTopByUserAndRecordDateOrderByRecordTimeDesc(User user, LocalDate recordDate);
    
    @Query("SELECT hr FROM HealthRecord hr WHERE hr.user = :user ORDER BY hr.recordTime DESC")
    List<HealthRecord> findByUserOrderByRecordTimeDesc(@Param("user") User user);
    
    @Query("SELECT hr FROM HealthRecord hr WHERE hr.user = :user AND hr.recordDate BETWEEN :startDate AND :endDate ORDER BY hr.recordTime DESC")
    List<HealthRecord> findByUserAndRecordDateBetweenOrderByRecordTimeDesc(
            @Param("user") User user, 
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate);
}
