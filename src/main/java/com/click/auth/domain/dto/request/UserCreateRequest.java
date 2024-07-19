package com.click.auth.domain.dto.request;

import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;

import java.time.LocalDateTime;

public record UserCreateRequest(
        String identity,
        UserIdentityType type,
        String nickname,
        String passwd,
        String image
) {
    public User toEntity(String code, String salt) {
        return new User(
                null,
                identity,
                type,
                code,
                image,
                nickname,
                passwd,
                salt,
                700,
                LocalDateTime.now(),
                1,
                false
        );
    }
}
