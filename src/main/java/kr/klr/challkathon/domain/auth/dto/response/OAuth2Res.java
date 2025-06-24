package kr.klr.challkathon.domain.auth.dto.response;

public interface OAuth2Res {
    String getProvider();

    String getProviderId();

    String getNickname();

    String getImage();
}