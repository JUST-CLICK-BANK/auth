package com.click.auth.service;

import com.click.auth.domain.dao.UserDao;
import com.click.auth.domain.dto.response.LoginTokenResponse;
import com.click.auth.domain.dto.response.UserTokenResponse;
import com.click.auth.domain.entity.User;
import com.click.auth.exception.LoginExpirationException;
import com.click.auth.exception.PasswordMatchException;
import com.click.auth.util.JwtUtils;
import com.click.auth.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserDao userDao;
    private final JwtUtils jwtUtils;
    private final PasswordUtils passwordUtils;

    @Override
    public String generateUserToken(String accessToken, String password) {
        LoginTokenResponse loginToken = jwtUtils.parseLoginToken(accessToken);
        User user = userDao.selectUser(loginToken.uuid());
        String hashedPassword = passwordUtils.passwordHashing(password, user.getUserSalt());
        if (!user.getUserPasswd().equals(hashedPassword)) {
            throw new PasswordMatchException();
        }
        if (!user.getUserTokenVersion().equals(loginToken.version())) {
            throw new LoginExpirationException();
        }
        return jwtUtils.createUserToken(user);
    }

    @Override
    public UserTokenResponse parseUserToken(String userToken) {
        return jwtUtils.parseUserToken(userToken);
    }
}
