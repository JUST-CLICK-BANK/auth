package com.click.auth.domain.dto.response;

public record UserCreateInitDataResponse(
    String identity,
    String type
) {
    public static UserCreateInitDataResponse fromKakao(KakaoTokenInfoResponse res) {
        return new UserCreateInitDataResponse(
                res.id().toString(),
                "kakao"
        );
    }
}
