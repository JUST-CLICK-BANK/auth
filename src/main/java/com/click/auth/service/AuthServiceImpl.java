package com.click.auth.service;

import com.click.auth.domain.dto.LoginTokenDto;
import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.domain.dto.response.UserInfoResponse;
import com.click.auth.domain.entity.User;
import com.click.auth.domain.repository.UserRepository;
import com.click.auth.domain.type.UserIdentityType;
import com.click.auth.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    public User findUserByIdentity(String identity, UserIdentityType type) {
        return userRepository.findByUserIdentityAndUserIdentityType(identity, type)
                .orElse(null);
    }

    @Override
    public User findUserByUuid(UUID userId) {
        return null;
    }

    @Override
    public String createUser(UserCreateRequest req) {
        User user = req.toEntity();
        userRepository.save(user);
        return jwtUtils.createLoginToken(LoginTokenDto.from(user));
    }

    @Override
    public String generateLoginToken(String identity, UserIdentityType type) {
        User user = findUserByIdentity(identity, type);
        return jwtUtils.createLoginToken(LoginTokenDto.from(user));
    }

    @Override
    public String generateUserToken(String accessToken, String password) {
        return "";
    }

    @Override
    public UserInfoResponse parseUserToken(String userToken) {
        return null;
    }
}
