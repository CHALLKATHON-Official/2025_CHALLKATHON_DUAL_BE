package kr.klr.challkathon.domain.auth.service;

import kr.klr.challkathon.domain.auth.dto.CustomUserDetails;
import kr.klr.challkathon.domain.auth.dto.response.OAuth2Res;
import kr.klr.challkathon.domain.auth.dto.KakaoResponse;
import kr.klr.challkathon.domain.auth.dto.GoogleResponse;
import kr.klr.challkathon.domain.auth.exception.OAuth2Exception;
import kr.klr.challkathon.domain.auth.utils.AuthUtil;
import kr.klr.challkathon.domain.auth.utils.JwtUtil;
import kr.klr.challkathon.domain.user.entity.User;
import kr.klr.challkathon.domain.user.service.UserService;
import kr.klr.challkathon.domain.user.dto.request.UpdateUserInfoReq;
import com.github.f4b6a3.ulid.UlidCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {
    
    private final UserService userService;
    private final AuthUtil authUtil;
    private final JwtUtil jwtUtil;
    private final ClientRegistrationRepository clientRegistrationRepository;
    
    // DefaultOAuth2UserService를 직접 생성해서 사용 (Bean 주입이 안 되므로)
    private final DefaultOAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();

    /**
     * OAuth2 인증 후 JWT 토큰 생성 (Spring Boot 자동 설정 활용)
     */
    public String processOAuth2User(String registrationId, OAuth2AccessToken accessToken) {
        try {
            // ClientRegistration 조회 (Spring Boot 자동 설정)
            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
            if (clientRegistration == null) {
                throw OAuth2Exception.providerNotSupported(registrationId);
            }

            // OAuth2UserRequest 생성
            OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);
            
            // 사용자 정보 조회 (Spring Boot가 자동으로 등록한 DefaultOAuth2UserService 사용)
            OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
            
            // OAuth2Res로 변환
            OAuth2Res oAuth2Res = createOAuth2Response(registrationId, oAuth2User.getAttributes());
            
            // 사용자 처리
            CustomUserDetails userDetails = processUser(oAuth2Res);
            
            // JWT 토큰 생성
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            return jwtUtil.createToken(userDetails.getUid(), userDetails.getUsername(), role, registrationId);
            
        } catch (Exception e) {
            log.error("OAuth2 사용자 처리 중 오류: registrationId={}, error={}", registrationId, e.getMessage(), e);
            throw OAuth2Exception.userInfoFailed(registrationId, e.getMessage());
        }
    }

    /**
     * registrationId와 attributes를 기반으로 OAuth2Res 생성
     */
    private OAuth2Res createOAuth2Response(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "kakao" -> new KakaoResponse(attributes);
            case "google" -> new GoogleResponse(attributes);
            default -> throw OAuth2Exception.providerNotSupported(registrationId);
        };
    }

    /**
     * OAuth2Res를 기반으로 사용자 생성/업데이트
     */
    private CustomUserDetails processUser(OAuth2Res oAuth2Res) {
        String username = oAuth2Res.getProvider() + "_" + oAuth2Res.getProviderId();
        String nickname = oAuth2Res.getNickname();
        String image = oAuth2Res.getImage();
        String provider = oAuth2Res.getProvider();

        // 사용자명 검증
        String lcUsername = authUtil.getLowerCaseUserId(username);
        User existUser = userService.findByUsernameAndIsDeletedFalseOrGetNull(lcUsername);

        final String uid;
        if (existUser == null) {
            // 신규 사용자 생성
            uid = createNewUser(lcUsername, provider, nickname, image);
            log.info("신규 OAuth 사용자 생성: username={}, provider={}", lcUsername, provider);
        } else {
            // 기존 사용자 정보 업데이트
            updateExistingUser(existUser, nickname, image);
            uid = existUser.getUid();
            log.info("기존 OAuth 사용자 정보 업데이트: username={}, provider={}", lcUsername, provider);
        }

        return new CustomUserDetails(uid, lcUsername, nickname, image, "ROLE_USER");
    }

    /**
     * 신규 사용자 생성
     */
    private String createNewUser(String username, String provider, String nickname, String image) {
        String ulid = UlidCreator.getUlid().toString();
        
        // UserService의 createUser 메서드 시그니처에 맞춰 호출
        User newUser = userService.createUser(ulid, username, "", provider);
        
        // 초기 프로필 정보 설정
        if (nickname != null || image != null) {
            UpdateUserInfoReq updateReq = UpdateUserInfoReq.builder()
                    .nickname(nickname)
                    .profileImage(image)
                    .build();
            userService.updateUserInfo(newUser.getUid(), updateReq);
        }
        
        return newUser.getUid();
    }

    /**
     * 기존 사용자 정보 업데이트
     */
    private void updateExistingUser(User existUser, String nickname, String image) {
        UpdateUserInfoReq updateReq = UpdateUserInfoReq.builder()
                .nickname(nickname)
                .profileImage(image)
                .build();
        userService.updateUserInfo(existUser.getUid(), updateReq);
    }

    /**
     * 지원하는 OAuth 제공자 목록 반환
     */
    public String[] getSupportedProviders() {
        return new String[]{"kakao", "google"};
    }

    /**
     * 특정 제공자의 OAuth 인증 URL 생성 (OAuth2 Client 사용)
     */
    public String getAuthorizationUrl(String registrationId) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
        if (clientRegistration == null) {
            throw OAuth2Exception.providerNotSupported(registrationId);
        }

        // OAuth2 Client의 ClientRegistration 정보를 사용하여 URL 생성
        String authorizationUri = clientRegistration.getProviderDetails().getAuthorizationUri();
        String clientId = clientRegistration.getClientId();
        String redirectUri = clientRegistration.getRedirectUri();
        String scopes = String.join(" ", clientRegistration.getScopes()); // 공백으로 구분

        return authorizationUri +
               "?client_id=" + clientId +
               "&redirect_uri=" + redirectUri +
               "&response_type=code" +
               "&scope=" + scopes;
    }
}
