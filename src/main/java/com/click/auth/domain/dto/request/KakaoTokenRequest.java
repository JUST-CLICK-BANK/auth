package com.click.auth.domain.dto.request;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class KakaoTokenRequest {
    private final String grant_type;
    private final String client_id;
    private final String redirect_uri;
    private final String code;

    public KakaoTokenRequest(
            @Value("${kakao.client_id}") String client_id,
            @Value("${kakao.redirect_uri}") String redirect_uri,
            String code
    ) {
        this.grant_type = "authorization_code";
        this.client_id = client_id;
        this.redirect_uri = redirect_uri;
        this.code = code;
    }
}
