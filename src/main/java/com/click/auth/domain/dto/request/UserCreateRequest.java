package com.click.auth.domain.dto.request;

import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;

import com.click.auth.util.PasswordUtils;
import java.time.LocalDateTime;

public record UserCreateRequest(
    String identity,
    UserIdentityType type,
    String nickname,
    String passwd,
    String image,
    String account
) {

    public User toEntity(String code, PasswordUtils passwordUtils) {
        String salt = passwordUtils.generateSalt();
        return new User(
            null,
            identity,
            type,
            code,
            image,
            nickname,
            passwordUtils.passwordHashing(passwd, salt),
            salt,
            700,
            LocalDateTime.now(),
            1,
            false,
            account
        );
    }
    public User toEntity(String code, String hashedPassword, String salt) {
        return new User(
            null,
            identity,
            type,
            code,
            image,
            nickname,
            hashedPassword,
            salt,
            700,
            LocalDateTime.now(),
            1,
            false,
            account
        );
    }
}
