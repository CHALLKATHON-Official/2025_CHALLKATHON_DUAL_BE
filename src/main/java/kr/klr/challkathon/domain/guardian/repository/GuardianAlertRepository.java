package kr.klr.challkathon.domain.guardian.repository;

import kr.klr.challkathon.domain.guardian.entity.GuardianAlert;
import kr.klr.challkathon.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GuardianAlertRepository extends JpaRepository<GuardianAlert, String> {
    
    @Query("SELECT ga FROM GuardianAlert ga WHERE ga.guardian = :guardian ORDER BY ga.alertTime DESC")
    List<GuardianAlert> findByGuardianOrderByAlertTimeDesc(@Param("guardian") User guardian);
    
    @Query("SELECT ga FROM GuardianAlert ga WHERE ga.guardian = :guardian AND ga.isRead = false ORDER BY ga.alertTime DESC")
    List<GuardianAlert> findUnreadAlertsByGuardian(@Param("guardian") User guardian);
    
    @Query("SELECT ga FROM GuardianAlert ga WHERE ga.patient = :patient ORDER BY ga.alertTime DESC")
    List<GuardianAlert> findByPatientOrderByAlertTimeDesc(@Param("patient") User patient);
    
    @Query("SELECT ga FROM GuardianAlert ga WHERE ga.guardian = :guardian AND ga.alertTime >= :since ORDER BY ga.alertTime DESC")
    List<GuardianAlert> findRecentAlertsByGuardian(@Param("guardian") User guardian, @Param("since") LocalDateTime since);
}
