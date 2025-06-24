package kr.klr.challkathon.domain.auth.dto;

import java.util.Map;
import kr.klr.challkathon.domain.auth.dto.response.OAuth2Res;

public class GoogleResponse implements OAuth2Res {
    private final Map<String,Object> attributes;

    public GoogleResponse(Map<String,Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() { 
        return "google"; 
    }
    
    @Override
    public String getProviderId() { 
        Object sub = attributes.get("sub");
        return sub != null ? sub.toString() : "";
    }
    
    @Override
    public String getNickname() { 
        Object name = attributes.get("name");
        return name != null ? name.toString() : "Google 사용자";
    }
    
    @Override
    public String getImage() { 
        Object picture = attributes.get("picture");
        return picture != null ? picture.toString() : "";
    }
}
