package kr.klr.challkathon.domain.user.entity;

import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import kr.klr.challkathon.global.entity.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user")
@Table(name = "user", indexes = {
      @Index(name = "idx_user_email", columnList = "email")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseTime {

	@Id
	@Column(name = "uid", nullable = false, length = 26, unique = true)
	@Builder.Default
	private String uid = UlidCreator.getUlid().toString();

	@Column(name = "username", nullable = false, length = 30)
	private String username;

	@Column(name = "email")
	private String email;

	@Column(name = "provider")
	private String provider;

	@Column(name = "role")
	private String role;

	@Setter
	@Column(name = "nickname", length = 50)
	private String nickname;

	@Setter
	@Column(name = "profile_image", length = 255)
	private String profileImage;

	// === 환자 정보 ===
	@Column(name = "patient_age")
	private Integer patientAge;

	@Column(name = "patient_birth_date")
	private LocalDate patientBirthDate;

	@Column(name = "patient_disease", length = 200)
	private String patientDisease;

	@Column(name = "patient_phone_number", length = 20)
	private String patientPhoneNumber;

	@Column(name = "patient_emergency_contact", length = 20)
	private String patientEmergencyContact;

	@Column(name = "is_patient_profile_complete", nullable = false)
	@Builder.Default
	private Boolean isPatientProfileComplete = false;

	// === 보호자 정보 ===
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guardian_target_patient_uid", referencedColumnName = "uid")
	private User guardianTargetPatient; // 보호자로서 담당하는 환자

	@Column(name = "is_guardian_profile_complete", nullable = false)
	@Builder.Default
	private Boolean isGuardianProfileComplete = false;

	@Setter
	@Builder.Default
	private Boolean isDeleted = false;

	public UserInfo getInfo() {
		return new UserInfo(this.uid, this.username, this.nickname);
	}

	@PrePersist
	public void generateUid() {
	    if (this.uid == null) {
	        this.uid = UlidCreator.getUlid().toString();
	    }
	}

	// === 환자 관련 메서드 ===
	public boolean isPatient() {
		return isPatientProfileComplete;
	}

	public void setupPatientProfile(Integer age, String disease, String phoneNumber, String emergencyContact) {
		this.patientAge = age;
		this.patientDisease = disease;
		this.patientPhoneNumber = phoneNumber;
		this.patientEmergencyContact = emergencyContact;
		this.isPatientProfileComplete = true;
	}

	// === 보호자 관련 메서드 ===
	public boolean isGuardian() {
		return isGuardianProfileComplete;
	}

	public void setupGuardianProfile(User targetPatient) {
		this.guardianTargetPatient = targetPatient;
		this.isGuardianProfileComplete = true;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UserInfo {
		private String uid;
		private String username;
		private String nickname;
	}
}
