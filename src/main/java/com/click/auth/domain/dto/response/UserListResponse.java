package com.click.auth.domain.dto.response;

import com.click.auth.domain.entity.User;

import java.util.UUID;

public record UserListResponse(
    UUID id,
    String code,
    String img,
    String name
) {

    public static UserListResponse from(User user) {
        return new UserListResponse(
            user.getUserId(),
            user.getUserCode(),
            user.getUserImg(),
            user.getUserNickName()
        );
    }
}
