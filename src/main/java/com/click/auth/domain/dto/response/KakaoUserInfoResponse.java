package com.click.auth.domain.dto.response;

import java.time.LocalDateTime;

public record KakaoUserInfoResponse(
    Long id,
    LocalDateTime connected_at,
    KakaoUserAccount kakao_account
) {

}
