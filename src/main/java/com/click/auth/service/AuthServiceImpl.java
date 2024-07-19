package com.click.auth.service;

import com.click.auth.domain.dto.response.LoginTokenResponse;
import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.domain.dto.response.UserListResponse;
import com.click.auth.domain.dto.response.UserResponse;
import com.click.auth.domain.entity.User;
import com.click.auth.domain.repository.UserRepository;
import com.click.auth.domain.type.UserIdentityType;
import com.click.auth.exception.NotFoundExcetion;
import com.click.auth.util.FriendCodeUtils;
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

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final FriendCodeUtils friendCodeUtils;
    private final PasswordUtils passwordUtils;

    @Override
    @Transactional
    public String createUser(UserCreateRequest req) {
        String code = friendCodeUtils.generateCode();
        while (findUserByCode(code) != null) {
            code = friendCodeUtils.generateCode();
        }
        String salt = passwordUtils.generateSalt();
        User user = req.toEntity(code, passwordUtils.passwordHashing(req.passwd(), salt), salt);
        userRepository.save(user);
        return jwtUtils.createLoginToken(LoginTokenResponse.from(user));
    }

    @Override
    public User findUserByIdentity(String identity, UserIdentityType type) {
        return userRepository.findByUserIdentityAndUserIdentityType(identity, type).orElse(null);
    }

    @Override
    public User findUserByUuid(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundExcetion(("USER")));
    }

    @Override
    public UserResponse findUserByCode(String code) {
        return UserResponse.from(userRepository.findByUserCode(code).orElse(null));
    }

    @Override
    public List<UserListResponse> findUsersByCodes(String[] codes) {
        List<User> allByUserCode = userRepository.findAllByUserCodeIn(codes);
        return allByUserCode.stream().map(UserListResponse::from).toList();
//        return userRepository.findAllByUserCode(codes).stream().map(UserListResponse::from).toList();
    }

    @Override
    @Transactional
    public void updateUserImage(UUID id, String image) {
        User user = findUserByUuid(id);
        user.setImage(image);
    }

    @Override
    @Transactional
    public void updateUserNickname(UUID id, String name) {
        User user = findUserByUuid(id);
        user.setNickname(name);
    }

    @Override
    @Transactional
    public void updateUserPassword(UUID id, String password) {
        User user = findUserByUuid(id);
        String salt = passwordUtils.generateSalt();
        user.setPassword(passwordUtils.passwordHashing(password, salt), salt);
        user.upTokenVersion();
    }

    @Override
    @Transactional
    public void updateTokenVersion(UUID id) {
        User user = findUserByUuid(id);
        user.upTokenVersion();
    }

    @Override
    @Transactional
    public void disableUser(UUID id) {
        User user = findUserByUuid(id);
        user.disable();
    }
}
