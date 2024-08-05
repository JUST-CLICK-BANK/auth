package com.click.auth.domain.dto.response;

import io.jsonwebtoken.Claims;

import java.util.UUID;

public record UserTokenResponse(
    UUID id,
    String code,
    String img,
    String name,
    String createdAt,
    Integer rank,
    String account
) {

    static public UserTokenResponse from(Claims claims) {
        return new UserTokenResponse(
            UUID.fromString(claims.get("id", String.class)),
            claims.get("code", String.class),
            claims.get("img", String.class),
            claims.get("name", String.class),
            claims.get("createdAt", String.class),
            claims.get("rank", Integer.class),
            claims.get("account", String.class)
        );
    }
}
