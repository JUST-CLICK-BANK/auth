package com.click.auth.service;

import com.click.auth.domain.dto.response.UserInfoResponse;
import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;

import java.util.UUID;

public interface AuthService {
    User findUserByIdentity(String identity, UserIdentityType type);
    User findUserByUuid(UUID userId);
    User createUser();
    String generateAccessToken(String identity, UserIdentityType type, String passwd);
    String generateUserToken(String accessToken);
    UserInfoResponse parseUserToken(String userToken);
}
