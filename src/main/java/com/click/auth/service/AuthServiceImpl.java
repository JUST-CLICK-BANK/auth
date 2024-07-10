package com.click.auth.service;

import com.click.auth.domain.dto.response.UserInfoResponse;
import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;

import java.util.UUID;

public class AuthServiceImpl implements AuthService{
    @Override
    public User findUserByIdentity(String identity, UserIdentityType type) {
        return null;
    }

    @Override
    public User findUserByUuid(UUID userId) {
        return null;
    }

    @Override
    public User createUser() {
        return null;
    }

    @Override
    public String generateAccessToken(String identity, UserIdentityType type, String passwd) {
        return "";
    }

    @Override
    public String generateUserToken(String accessToken) {
        return "";
    }

    @Override
    public UserInfoResponse parseUserToken(String userToken) {
        return null;
    }
}
