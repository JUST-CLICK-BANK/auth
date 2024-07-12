package com.click.auth.service;

import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.domain.dto.response.UserResponse;
import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;

import java.util.UUID;

public interface AuthService {
    String createUser(UserCreateRequest req);
    User findUserByIdentity(String identity, UserIdentityType type);
    User findUserByUuid(UUID userId);
    UserResponse findUserByCode(String code);
    void updateUserProfile(UUID id, String image, String name);
    void disableUser(UUID id);
}
