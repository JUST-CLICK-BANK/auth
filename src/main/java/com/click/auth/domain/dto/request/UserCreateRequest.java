package com.click.auth.domain.dto.request;

import com.click.auth.domain.type.UserIdentityType;

public record UserCreateRequest(
        String identity,
        UserIdentityType type,
        String nickname,
        String passwd
) {
}
