package com.click.auth.service;

import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.domain.dto.response.UserListResponse;
import com.click.auth.domain.dto.response.UserResponse;
import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;

import java.util.List;
import java.util.UUID;

public interface AuthService {
    String createUser(UserCreateRequest req);
    User findUserByIdentity(String identity, UserIdentityType type);
    User findUserByUuid(UUID userId);
    UserResponse findUserByCode(String code);
    List<UserListResponse> findUsersByCodes(String[] codes);
    void updateUserImage(UUID id, String image);
    void updateUserNickname(UUID id, String name);
    void updateUserPassword(UUID id, String password);
    void updateTokenVersion(UUID id);
    void disableUser(UUID id);
}
