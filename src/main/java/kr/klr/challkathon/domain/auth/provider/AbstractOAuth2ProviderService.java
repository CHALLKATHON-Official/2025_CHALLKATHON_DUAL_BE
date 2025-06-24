package kr.klr.challkathon.domain.auth.provider;

import java.util.Map;
import kr.klr.challkathon.domain.auth.dto.response.OAuth2Res;
import kr.klr.challkathon.domain.auth.exception.OAuth2Exception;
import kr.klr.challkathon.global.globalResponse.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractOAuth2ProviderService implements OAuthProviderService {
    protected final String clientId;
    protected final String clientSecret;
    protected final String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getAccessToken(String code) {
        try {
            MultiValueMap<String,String> params = buildTokenParams(code);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String,String>> req = new HttpEntity<>(params, headers);
            Map<String,Object> res = restTemplate.postForObject(getTokenUri(), req, Map.class);
            
            if (res == null || !res.containsKey("access_token")) {
                log.error("액세스 토큰 응답이 올바르지 않음: provider={}, response={}", getProvider(), res);
                throw OAuth2Exception.accessTokenFailed(getProvider(), "토큰 응답이 올바르지 않습니다");
            }
            
            String accessToken = res.get("access_token").toString();
            log.debug("액세스 토큰 발급 성공: provider={}", getProvider());
            
            return accessToken;
            
        } catch (RestClientException e) {
            log.error("액세스 토큰 요청 실패: provider={}, error={}", getProvider(), e.getMessage());
            throw OAuth2Exception.accessTokenFailed(getProvider(), e.getMessage());
        } catch (Exception e) {
            log.error("액세스 토큰 처리 중 예상치 못한 오류: provider={}, error={}", getProvider(), e.getMessage());
            throw OAuth2Exception.accessTokenFailed(getProvider(), e.getMessage());
        }
    }

    @Override
    public OAuth2Res getOAuth2Res(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<Void> req = new HttpEntity<>(headers);

            Map<String,Object> attrs = restTemplate.exchange(
                getUserInfoUri(), HttpMethod.GET, req, Map.class).getBody();

            if (attrs == null || attrs.isEmpty()) {
                log.error("사용자 정보 응답이 비어있음: provider={}", getProvider());
                throw OAuth2Exception.userInfoFailed(getProvider(), "사용자 정보를 가져올 수 없습니다");
            }

            OAuth2Res result = parseOAuth2Res(attrs);
            log.debug("사용자 정보 조회 성공: provider={}, providerId={}", getProvider(), result.getProviderId());
            
            return result;
            
        } catch (RestClientException e) {
            log.error("사용자 정보 요청 실패: provider={}, error={}", getProvider(), e.getMessage());
            throw OAuth2Exception.userInfoFailed(getProvider(), e.getMessage());
        } catch (Exception e) {
            log.error("사용자 정보 처리 중 예상치 못한 오류: provider={}, error={}", getProvider(), e.getMessage());
            throw OAuth2Exception.userInfoFailed(getProvider(), e.getMessage());
        }
    }

    /** 토큰 교환용 URI */
    protected abstract String getTokenUri();

    /** 사용자 정보 조회용 URI */
    protected abstract String getUserInfoUri();

    /** JSON → OAuth2Res 변환 */
    protected abstract OAuth2Res parseOAuth2Res(Map<String,Object> attributes);

    /** token 요청 파라미터 세팅 */
    protected abstract MultiValueMap<String,String> buildTokenParams(String code);
}