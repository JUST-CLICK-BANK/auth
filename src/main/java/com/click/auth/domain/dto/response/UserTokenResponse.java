package com.click.auth.domain.dto.response;

import io.jsonwebtoken.Claims;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserTokenResponse(
        UUID id,
        String code,
        String img,
        String name,
        LocalDateTime createdAt,
        Integer rank
) {
    static public UserTokenResponse from(Claims claims) {
        return new UserTokenResponse(
                claims.get("id", UUID.class),
                claims.get("code", String.class),
                claims.get("img", String.class),
                claims.get("name", String.class),
                claims.get("createdAt", LocalDateTime.class),
                claims.get("rank", Integer.class)
        );
    }
}
