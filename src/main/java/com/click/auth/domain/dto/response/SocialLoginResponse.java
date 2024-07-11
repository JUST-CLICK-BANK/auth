package com.click.auth.domain.dto.response;

import com.click.auth.domain.type.UserIdentityType;
import io.jsonwebtoken.Claims;

public record SocialLoginResponse(
        Boolean isAlready,
        String identity,
        UserIdentityType type
) {
    public static SocialLoginResponse from(KakaoTokenInfoResponse res, boolean isAleady) {
        return new SocialLoginResponse(
                isAleady,
                res.id().toString(),
                UserIdentityType.KAKAO
        );
    }

    public static SocialLoginResponse fromToken(Claims claims){
        return new SocialLoginResponse(
            false,
                claims.get("identity", String.class),
                claims.get("type", UserIdentityType.class)
        );
    }
}
