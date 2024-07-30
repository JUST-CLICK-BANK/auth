package com.click.auth.service;

import static com.click.auth.util.FriendCodeUtils.generateCode;

import com.click.auth.domain.dao.UserDao;
import com.click.auth.domain.dto.response.LoginTokenResponse;
import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.domain.dto.response.UserListResponse;
import com.click.auth.domain.dto.response.UserResponse;
import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;
import com.click.auth.exception.NotFoundExcetion;
import com.click.auth.util.JwtUtils;
import com.click.auth.util.PasswordUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final JwtUtils jwtUtils;
    private final PasswordUtils passwordUtils;

    @Override
    @Transactional
    public String createUser(UserCreateRequest req) {
        String code = generateCode();
        while (findUserByCode(code) != null) {
            code = generateCode();
        }
        User user = req.toEntity(code, passwordUtils);
        userDao.insertUser(user);
        LoginTokenResponse tokenResponse = LoginTokenResponse.from(user);
        return jwtUtils.createLoginToken(tokenResponse);
    }

    @Override
    public User findUserByIdentity(String identity, UserIdentityType type) {
        return userDao.selectOptionalUser(identity, type).orElseThrow(() -> new NotFoundExcetion("USER"));
    }

    @Override
    public User findUserByUuid(UUID userId) {
        return userDao.selectUser(userId);
    }

    @Override
    public UserResponse findUserByCode(String code) {
        return UserResponse.from(userDao.selectOptionalUser(code).orElse(null));
    }

    @Override
    public List<UserListResponse> findUsersByCodes(String[] codes) {
        List<User> allByUserCode = userDao.selectAllUser(codes);
        return allByUserCode.stream().map(UserListResponse::from).toList();
    }

    @Override
    @Transactional
    public void updateUserImage(UUID id, String image) {
        userDao.updateUserImage(id, image);
    }

    @Override
    @Transactional
    public void updateUserNickname(UUID id, String name) {
        userDao.updateUserNickname(id, name);
    }

    @Override
    @Transactional
    public void updateUserPassword(UUID id, String password) {
        userDao.updateUserPassword(id, password);
    }

    @Override
    @Transactional
    public void updateTokenVersion(UUID id) {
        userDao.updateUserTokenVersion(id);
    }

    @Override
    @Transactional
    public void disableUser(UUID id) {
        userDao.deleteUser(id);
    }
}
