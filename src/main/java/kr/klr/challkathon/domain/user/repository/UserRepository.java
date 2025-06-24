package kr.klr.challkathon.domain.user.repository;

import kr.klr.challkathon.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    /**
     * username으로 삭제되지 않은 사용자 조회
     */
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
    
    /**
     * uid로 사용자 조회
     */
    Optional<User> findByUid(String uid);
    
    /**
     * username으로 사용자 존재 여부 확인 (삭제되지 않은 사용자만)
     */
    boolean existsByUsernameAndIsDeletedFalse(String username);
    
    /**
     * email로 삭제되지 않은 사용자 조회
     */
    Optional<User> findByEmailAndIsDeletedFalse(String email);
}
