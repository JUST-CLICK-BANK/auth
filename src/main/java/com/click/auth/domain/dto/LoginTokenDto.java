package com.click.auth.domain.dto;

import com.click.auth.domain.entity.User;
import io.jsonwebtoken.Claims;

import java.util.UUID;

public record LoginTokenDto(
        UUID uuid,
        String password,
        Integer version
) {
    public static LoginTokenDto from(User user){
        return new LoginTokenDto(
                user.getUserId(),
                user.getUserPasswd(),
                user.getUserTokenVersion()
        );
    }

    public static LoginTokenDto from(Claims claims) {
        return new LoginTokenDto(
                claims.get("uuid", UUID.class),
                claims.get("password", String.class),
                claims.get("version", Integer.class)
        );
    }
}
