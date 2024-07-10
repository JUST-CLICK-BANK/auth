package com.click.auth.domain.dto.request;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class KakaoTokenRequest {
    private final String grant_type;
    @Value("${kakao.client_id}") private String client_id;
    @Value("${kakao.redirect_uri}") private String redirect_uri;
    private final String code;

    public KakaoTokenRequest(String code) {
        this.grant_type = "authorization_code";
        this.code = code;
    }
}
