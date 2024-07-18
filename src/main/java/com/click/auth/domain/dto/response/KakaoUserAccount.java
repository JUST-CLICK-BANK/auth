package com.click.auth.domain.dto.response;

public record KakaoUserAccount(
    boolean profile_needs_agreement,
    KakaoUserProfile profile
) {

}
