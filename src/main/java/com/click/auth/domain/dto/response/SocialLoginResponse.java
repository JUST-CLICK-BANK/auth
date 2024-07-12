package com.click.auth.domain.dto.response;

import com.click.auth.domain.type.UserIdentityType;

public record SocialLoginResponse(
        Boolean isAlready,
        String identity,
        UserIdentityType type,
        String nickname,
        String image
) {
    public static SocialLoginResponse from(KakaoTokenInfoResponse res, boolean isAlready) {
        return new SocialLoginResponse(
                isAlready,
                res.id().toString(),
                UserIdentityType.KAKAO,
                null,
                null
        );
    }

    public static SocialLoginResponse from(KakaoUserInfoResponse res, boolean isAlready) {
        return new SocialLoginResponse(
                isAlready,
                res.id().toString(),
                UserIdentityType.KAKAO,
                res.kakao_account().profile().is_default_nickname() ? null : res.kakao_account().profile().nickname(),
                res.kakao_account().profile().is_default_image() ? null : res.kakao_account().profile().profile_image_url()
        );
    }
}
