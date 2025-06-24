package kr.klr.challkathon.domain.user.entity;

import com.github.f4b6a3.ulid.UlidCreator;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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

	@Getter
	@AllArgsConstructor
	public static class UserInfo {
		private String uid;
		private String username;
		private String nickname;
	}
}