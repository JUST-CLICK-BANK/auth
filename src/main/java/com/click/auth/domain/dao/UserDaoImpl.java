package com.click.auth.domain.dao;

import com.click.auth.domain.entity.User;
import com.click.auth.domain.repository.UserRepository;
import com.click.auth.domain.type.UserIdentityType;
import com.click.auth.exception.DeletedUserException;
import com.click.auth.exception.NotFoundExcetion;
import com.click.auth.util.PasswordUtils;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;

    private final PasswordUtils passwordUtils;

    @Override
    public User selectUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundExcetion("USER"));
        if (user.getIsDisable()) {
            throw new DeletedUserException();
        }
        return user;
    }

    @Override
    public Optional<User> selectOptionalUser(String code) {
        return userRepository.findByUserCode(code);
    }

    @Override
    public Optional<User> selectOptionalUser(String identity, UserIdentityType type) {
        return userRepository.findByUserIdentityAndUserIdentityType(identity, type);
    }

    @Override
    public List<User> selectAllUser(String[] code) {
        return userRepository.findAllByUserCodeInAndIsDisable(code, false);
    }

    @Override
    public User insertUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUserTokenVersion(UUID id) {
        User user = selectUser(id);
        user.upTokenVersion();
        return userRepository.save(user);
    }

    @Override
    public User updateUserPassword(UUID id, String password) {
        User user = selectUser(id);
        String salt = passwordUtils.generateSalt();
        user.setPassword(passwordUtils.passwordHashing(password, salt), salt);
        return userRepository.save(user);
    }

    @Override
    public User updateUserImage(UUID id, String image) {
        User user = selectUser(id);
        user.setImage(image);
        return userRepository.save(user);
    }

    @Override
    public User updateUserNickname(UUID id, String nickname) {
        User user = selectUser(id);
        user.setNickname(nickname);
        return userRepository.save(user);
    }

    @Override
    public User updateUserRank(UUID id, Integer rank) {
        User user = selectUser(id);
        user.setRank(rank);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundExcetion("USER"));
        user.disable();
    }
}
