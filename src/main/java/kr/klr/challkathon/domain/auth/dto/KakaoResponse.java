package kr.klr.challkathon.domain.auth.dto;

import java.util.Map;
import kr.klr.challkathon.domain.auth.dto.response.OAuth2Res;

public class KakaoResponse implements OAuth2Res {
    private final Map<String, Object> attribute;
    private final Map<String, Object> properties;

    @SuppressWarnings("unchecked")
    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
        this.properties = (Map<String, Object>) attribute.get("properties");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        Object id = attribute.get("id");
        return id != null ? id.toString() : "";
    }

    @Override
    public String getNickname() {
        if (properties == null) {
            return "카카오 사용자";
        }
        Object nickname = properties.get("nickname");
        return nickname != null ? nickname.toString() : "카카오 사용자";
    }

    @Override
    public String getImage() {
        if (properties == null) {
            return "";
        }
        Object profileImage = properties.get("profile_image");
        return profileImage != null ? profileImage.toString() : "";
    }
}
