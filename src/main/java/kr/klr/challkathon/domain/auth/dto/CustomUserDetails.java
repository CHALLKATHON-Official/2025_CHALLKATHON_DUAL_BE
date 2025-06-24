package kr.klr.challkathon.domain.auth.dto;

import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {
    private final String uid;
    private final String username;
    private final String image;
    private final String nickname;
    private final String role;

    public CustomUserDetails(String uid, String username, String nickname, String image, String role) {
        this.uid      = uid;
        this.username = username;
        this.nickname = nickname;
        this.image    = image;
        this.role     = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role);
    }

    @Override
    public String getPassword() {
        return ""; // OAuth 흐름이라 실제 패스워드 불필요
    }

    @Override
    public String getUsername() {
        return username;
    }

    // 계정 만료/잠김 여부는 모두 활성화 상태로 간주
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }
}