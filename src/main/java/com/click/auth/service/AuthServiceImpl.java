package com.click.auth.service;

import com.click.auth.domain.dto.response.LoginTokenResponse;
import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.domain.dto.response.UserTokenResponse;
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
    public User findUserByCode(UUID userId) {
        return null;
    }

    @Override
    public String createUser(UserCreateRequest req) {
        User user = req.toEntity();
        userRepository.save(user);
        return jwtUtils.createLoginToken(LoginTokenResponse.from(user));
    }

    @Override
    public String generateLoginToken(String identity, UserIdentityType type) {
        User user = findUserByIdentity(identity, type);
        return jwtUtils.createLoginToken(LoginTokenResponse.from(user));
    }

    @Override
    public String generateUserToken(String accessToken, String password) {
        LoginTokenResponse loginToken = jwtUtils.parseLoginToken(accessToken);
        User user = userRepository.findById(loginToken.uuid()).orElseThrow(IllegalArgumentException::new);
        if (!user.getUserPasswd().equals(password)) {
            throw new IllegalArgumentException();
        }
        if (!user.getUserTokenVersion().equals(loginToken.version())) {
            throw new IllegalArgumentException();
        }
        return jwtUtils.createUserToken(user);
    }

    @Override
    public UserTokenResponse parseUserToken(String userToken) {
        return jwtUtils.parseUserToken(userToken);
    }
}
