package kr.klr.challkathon.domain.dashboard.repository;

import kr.klr.challkathon.domain.dashboard.entity.DailyStats;
import kr.klr.challkathon.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyStatsRepository extends JpaRepository<DailyStats, String> {
    
    Optional<DailyStats> findByUserAndStatsDate(User user, LocalDate statsDate);
    
    @Query("SELECT ds FROM DailyStats ds WHERE ds.user = :user AND ds.statsDate BETWEEN :startDate AND :endDate ORDER BY ds.statsDate")
    List<DailyStats> findByUserAndStatsDateBetween(@Param("user") User user, 
                                                   @Param("startDate") LocalDate startDate, 
                                                   @Param("endDate") LocalDate endDate);
    
    @Query("SELECT ds FROM DailyStats ds WHERE ds.user = :user ORDER BY ds.statsDate DESC")
    List<DailyStats> findByUserOrderByStatsDateDesc(@Param("user") User user);
    
    @Query("SELECT SUM(ds.totalSteps) FROM DailyStats ds WHERE ds.user = :user AND ds.statsDate BETWEEN :startDate AND :endDate")
    Optional<Long> sumStepsByUserAndDateRange(@Param("user") User user, 
                                            @Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate);
}
