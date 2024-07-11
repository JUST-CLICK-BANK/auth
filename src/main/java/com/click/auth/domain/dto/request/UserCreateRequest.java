package com.click.auth.domain.dto.request;

import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;

import java.time.LocalDateTime;

public record UserCreateRequest(
        String identity,
        UserIdentityType type,
        String nickname,
        String passwd
) {
    public User toEntity() {
        return new User(
                null,
                identity,
                type,
                "",
                "",
                nickname,
                passwd,
                700,
                LocalDateTime.now(),
                1,
                false
        );
    }
}
