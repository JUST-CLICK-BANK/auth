package com.click.auth.domain.dto.response;

import com.click.auth.domain.entity.User;
import io.jsonwebtoken.Claims;

import java.util.UUID;

public record LoginTokenResponse(
        UUID uuid,
        Integer version
) {
    public static LoginTokenResponse from(User user){
        return new LoginTokenResponse(
                user.getUserId(),
                user.getUserTokenVersion()
        );
    }

    public static LoginTokenResponse from(Claims claims) {
        return new LoginTokenResponse(
                UUID.fromString(claims.get("uuid", String.class)),
                claims.get("version", Integer.class)
        );
    }
}
