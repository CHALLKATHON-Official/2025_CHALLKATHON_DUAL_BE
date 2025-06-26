package kr.klr.challkathon.domain.user.repository;

import kr.klr.challkathon.domain.user.entity.PatientLinkCode;
import kr.klr.challkathon.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientLinkCodeRepository extends JpaRepository<PatientLinkCode, String> {
    
    Optional<PatientLinkCode> findByLinkCodeAndIsUsedFalse(String linkCode);
    
    @Query("SELECT plc FROM PatientLinkCode plc WHERE plc.patient = :patient AND plc.isUsed = false AND plc.expiresAt > :now")
    Optional<PatientLinkCode> findValidCodeByPatient(@Param("patient") User patient, @Param("now") LocalDateTime now);
    
    @Query("SELECT plc FROM PatientLinkCode plc WHERE plc.expiresAt < :now AND plc.isUsed = false")
    List<PatientLinkCode> findExpiredCodes(@Param("now") LocalDateTime now);
    
    boolean existsByLinkCode(String linkCode);
}
