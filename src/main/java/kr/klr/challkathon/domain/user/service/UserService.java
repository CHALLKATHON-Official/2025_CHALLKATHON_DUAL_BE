package kr.klr.challkathon.domain.user.service;

import kr.klr.challkathon.domain.user.dto.request.PatientProfileSetupReq;
import kr.klr.challkathon.domain.user.dto.response.PatientLinkCodeRes;
import kr.klr.challkathon.domain.user.dto.response.UserProfileStatusRes;
import kr.klr.challkathon.domain.user.entity.PatientLinkCode;
import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.domain.user.repository.PatientLinkCodeRepository;
import kr.klr.challkathon.domain.user.repository.UserRepository;
import kr.klr.challkathon.domain.user.dto.request.UpdateUserInfoReq;
import kr.klr.challkathon.global.globalResponse.error.ErrorCode;
import kr.klr.challkathon.global.globalResponse.error.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    private final PatientLinkCodeRepository patientLinkCodeRepository;

    /**
     * username으로 삭제되지 않은 사용자 조회 (없으면 null 반환)
     */
    public User findByUsernameAndIsDeletedFalseOrGetNull(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElse(null);
    }

    /**
     * uid로 사용자 조회
     */
    public User findByUid(String uid) {
        return userRepository.findByUid(uid)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * 새 사용자 생성 (OAuth2 전용)
     */
    @Transactional
    public User createUser(String uid, String username, String email, String provider) {
        User user = User.builder()
                .uid(uid)
                .username(username)
                .email(email)
                .provider(provider)
                .role("ROLE_USER")
                .isDeleted(false)
                .build();
        
        return userRepository.save(user);
    }

    /**
     * 사용자 프로필 상태 조회
     */
    public UserProfileStatusRes getUserProfileStatus(User user) {
        UserProfileStatusRes.UserProfileStatusResBuilder builder = UserProfileStatusRes.builder()
                .isPatientProfileComplete(user.getIsPatientProfileComplete())
                .isGuardianProfileComplete(user.getIsGuardianProfileComplete());
        
        // 환자 프로필이 완성된 경우
        if (user.getIsPatientProfileComplete()) {
            builder.patientProfile(UserProfileStatusRes.PatientProfile.builder()
                    .age(user.getPatientAge())
                    .disease(user.getPatientDisease())
                    .phoneNumber(user.getPatientPhoneNumber())
                    .emergencyContact(user.getPatientEmergencyContact())
                    .build());
        }
        
        // 보호자 프로필이 완성된 경우
        if (user.getIsGuardianProfileComplete() && user.getGuardianTargetPatient() != null) {
            User targetPatient = user.getGuardianTargetPatient();
            builder.guardianProfile(UserProfileStatusRes.GuardianProfile.builder()
                    .targetPatientUid(targetPatient.getUid())
                    .targetPatientName(targetPatient.getNickname() != null ? 
                            targetPatient.getNickname() : targetPatient.getUsername())
                    .build());
        }
        
        return builder.build();
    }

    /**
     * 환자 프로필 설정
     */
    @Transactional
    public void setupPatientProfile(User user, PatientProfileSetupReq request) {
        user.setupPatientProfile(
                request.getAge(),
                request.getDisease(),
                request.getPhoneNumber(),
                request.getEmergencyContact()
        );
        userRepository.save(user);
    }

    /**
     * 사용자 정보 업데이트 (닉네임, 프로필 이미지)
     */
    @Transactional
    public void updateUserInfo(String uid, UpdateUserInfoReq req) {
        User user = findByUid(uid);
        
        if (req.getNickname() != null) {
            user.setNickname(req.getNickname());
        }
        
        if (req.getProfileImage() != null) {
            user.setProfileImage(req.getProfileImage());
        }
        
        userRepository.save(user);
    }

    /**
     * 환자 정보 업데이트
     */
    @Transactional
    public void updatePatientInfo(User user, PatientProfileSetupReq request) {
        if (!user.getIsPatientProfileComplete()) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "환자 프로필이 설정되지 않았습니다.");
        }
        
        user.setupPatientProfile(
                request.getAge(),
                request.getDisease(),
                request.getPhoneNumber(),
                request.getEmergencyContact()
        );
        userRepository.save(user);
    }

    /**
     * 환자 연동 코드 생성
     */
    @Transactional
    public PatientLinkCodeRes generatePatientLinkCode(User patient) {
        if (!patient.getIsPatientProfileComplete()) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "먼저 환자 프로필을 설정해주세요.");
        }
        
        // 기존 유효한 코드가 있는지 확인
        LocalDateTime now = LocalDateTime.now();
        patientLinkCodeRepository.findValidCodeByPatient(patient, now)
                .ifPresent(existingCode -> {
                    throw new GlobalException(ErrorCode.CONFLICT, "이미 유효한 연동 코드가 존재합니다.");
                });
        
        // 새 코드 생성
        String linkCode = generateUniqueCode();
        LocalDateTime expiresAt = now.plusHours(24); // 24시간 후 만료
        
        PatientLinkCode patientLinkCode = PatientLinkCode.builder()
                .patient(patient)
                .linkCode(linkCode)
                .expiresAt(expiresAt)
                .build();
        
        patientLinkCodeRepository.save(patientLinkCode);
        
        return PatientLinkCodeRes.builder()
                .linkCode(linkCode)
                .expiresAt(expiresAt)
                .message("연동 코드가 생성되었습니다. 24시간 내에 보호자가 사용해야 합니다.")
                .build();
    }

    /**
     * 환자와 보호자 연동
     */
    @Transactional
    public void linkPatientToGuardian(User guardian, String linkCode) {
        // 연동 코드 유효성 검증
        PatientLinkCode patientLinkCode = patientLinkCodeRepository.findByLinkCodeAndIsUsedFalse(linkCode)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND, "유효하지 않은 연동 코드입니다."));
        
        if (!patientLinkCode.isValid()) {
            throw new GlobalException(ErrorCode.BAD_REQUEST, "만료된 연동 코드입니다.");
        }
        
        // 보호자 프로필 설정
        guardian.setupGuardianProfile(patientLinkCode.getPatient());
        userRepository.save(guardian);
        
        // 연동 코드 사용 처리
        patientLinkCode.setIsUsed(true);
        patientLinkCodeRepository.save(patientLinkCode);
    }

    /**
     * 사용자 삭제 (soft delete)
     */
    @Transactional
    public void deleteUser(String uid) {
        User user = findByUid(uid);
        user.setIsDeleted(true);
        userRepository.save(user);
    }

    /**
     * 6자리 고유 코드 생성
     */
    private String generateUniqueCode() {
        Random random = new Random();
        String code;
        
        do {
            code = String.format("%06d", random.nextInt(1000000));
        } while (patientLinkCodeRepository.existsByLinkCode(code));
        
        return code;
    }

    /**
     * 만료된 연동 코드 정리 (스케줄러에서 호출)
     */
    @Transactional
    public void cleanupExpiredLinkCodes() {
        LocalDateTime now = LocalDateTime.now();
        patientLinkCodeRepository.findExpiredCodes(now)
                .forEach(code -> code.setIsUsed(true));
    }
}
