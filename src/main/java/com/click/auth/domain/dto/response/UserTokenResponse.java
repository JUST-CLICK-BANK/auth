package com.click.auth.domain.dto.response;

public record UserTokenResponse(
        String id
) {
    static public UserTokenResponse from(KakaoTokenInfoResponse res) {
        return  new UserTokenResponse(
                "asd"
        );
    }
}
