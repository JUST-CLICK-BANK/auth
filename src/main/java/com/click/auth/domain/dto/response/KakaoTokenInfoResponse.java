package com.click.auth.domain.dto.response;

public record KakaoTokenInfoResponse(
    Long id,
    Integer expires_in,
    Integer app_id
) {

}
