package com.click.auth.service;

import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.domain.dto.response.UserTokenResponse;
import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;

import java.util.UUID;

public interface AuthService {
    User findUserByIdentity(String identity, UserIdentityType type);
    User findUserByCode(UUID userId);
    String createUser(UserCreateRequest req);
    String generateLoginToken(String identity, UserIdentityType type);
    String generateUserToken(String accessToken, String password);
    UserTokenResponse parseUserToken(String userToken);
}
