package com.click.auth.domain.dto.response;

public record KakaoUserProfile(
        String nickname,
        String thumbnail_image_url,
        String profile_image_url,
        boolean is_default_image,
        boolean is_default_nickname
) {
}
