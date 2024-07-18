package com.click.auth.domain.dto.response;

import com.click.auth.domain.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String code,
    String img,
    String name,
    LocalDateTime createdAt
) {

    public static UserResponse from(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(
            user.getUserId(),
            user.getUserCode(),
            user.getUserImg(),
            user.getUserNickName(),
            user.getUserCreatedAt()
        );
    }
}
