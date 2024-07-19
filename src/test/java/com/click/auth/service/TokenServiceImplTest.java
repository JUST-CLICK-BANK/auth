package com.click.auth.service;

import com.click.auth.TestInitData;
import com.click.auth.domain.dto.response.LoginTokenResponse;
import com.click.auth.domain.dto.response.UserTokenResponse;
import com.click.auth.exception.LoginExpirationException;
import com.click.auth.exception.NotFoundExcetion;
import com.click.auth.exception.PasswordMatchException;
import com.click.auth.util.JwtUtils;
import com.click.auth.util.PasswordUtilsImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest extends TestInitData {

    @InjectMocks
    private TokenServiceImpl tokenService;
    @Mock
    private AuthServiceImpl authService;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private PasswordUtilsImpl passwordUtils;

    @Nested
    class generateUserToken {

        @Test
        void 성공_정상적으로_토큰_생성됨() {
            // give
            String accessToken = "aaaaaaaaaaaaaaaaaa";
            String password = "00000";
            String hashedPassword = "ce5fd1da07801ff3a9ea4e1a0c9f3d34b83ed0775f8ecc6efe1642baa815587d";
            String loginToken = "bbbbbbbbbbbbbbbbbb";
            LoginTokenResponse loginTokenResponse =
                new LoginTokenResponse(user.getUserId(), user.getUserTokenVersion());
            given(jwtUtils.parseLoginToken(accessToken)).willReturn(loginTokenResponse);
            given(authService.findUserByUuid(loginTokenResponse.uuid())).willReturn(user);
            given(passwordUtils.passwordHashing(password, user.getUserSalt()))
                .willReturn(hashedPassword);
            given(jwtUtils.createUserToken(user)).willReturn(loginToken);

            // when
            String response = tokenService.generateUserToken(accessToken, password);

            // then
            Mockito.verify(authService, Mockito.times(1))
                .findUserByUuid(loginTokenResponse.uuid());
            Mockito.verify(passwordUtils, Mockito.times(1))
                .passwordHashing(password, user.getUserSalt());
            assertEquals(loginToken, response);
        }

        @Test
        void 실패_유저가_존재하지_않음() {
            // give
            String accessToken = "zzzzzzzzzzzzzzzzzzz";
            String password = "00000";
            LoginTokenResponse loginTokenResponse =
                new LoginTokenResponse(user.getUserId(), user.getUserTokenVersion());
            given(jwtUtils.parseLoginToken(accessToken)).willReturn(loginTokenResponse);
            given(authService.findUserByUuid(loginTokenResponse.uuid()))
                .willThrow(NotFoundExcetion.class);

            // when
            assertThrows(NotFoundExcetion.class,
                () -> tokenService.generateUserToken(accessToken, password));

            // then
            Mockito.verify(authService, Mockito.times(1))
                .findUserByUuid(loginTokenResponse.uuid());
            Mockito.verify(passwordUtils, Mockito.times(0))
                .passwordHashing(password, user.getUserSalt());
        }

        @Test
        void 실패_비밀번호가_일치하지_않음() {
            // give
            String accessToken = "aaaaaaaaaaaaaaaaaa";
            String password = "11111";
            String hashedPassword = "ce5fd1da07801ff3a9ea4e1a0c9f3d34b83ed0775f8ecc6efe1642baa815587d";
            LoginTokenResponse loginTokenResponse =
                new LoginTokenResponse(user.getUserId(), 0);
            given(jwtUtils.parseLoginToken(accessToken)).willReturn(loginTokenResponse);
            given(authService.findUserByUuid(loginTokenResponse.uuid())).willReturn(user);
            given(passwordUtils.passwordHashing(password, user.getUserSalt()))
                .willReturn(hashedPassword);

            // when
            assertThrows(LoginExpirationException.class,
                () -> tokenService.generateUserToken(accessToken, password));

            // then
            Mockito.verify(authService, Mockito.times(1))
                .findUserByUuid(loginTokenResponse.uuid());
            Mockito.verify(passwordUtils, Mockito.times(1))
                .passwordHashing(password, user.getUserSalt());
            Mockito.verify(jwtUtils, Mockito.times(0)).parseUserToken(any());
        }

        @Test
        void 실패_토큰버전이_일치하지_않음() {
            // give
            String accessToken = "aaaaaaaaaaaaaaaaaa";
            String password = "00000";
            String hashedPassword = "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
            LoginTokenResponse loginTokenResponse =
                new LoginTokenResponse(user.getUserId(), user.getUserTokenVersion());
            given(jwtUtils.parseLoginToken(accessToken)).willReturn(loginTokenResponse);
            given(authService.findUserByUuid(loginTokenResponse.uuid())).willReturn(user);
            given(passwordUtils.passwordHashing(password, user.getUserSalt()))
                .willReturn(hashedPassword);

            // when
            assertThrows(PasswordMatchException.class,
                () -> tokenService.generateUserToken(accessToken, password));

            // then
            Mockito.verify(authService, Mockito.times(1))
                .findUserByUuid(loginTokenResponse.uuid());
            Mockito.verify(passwordUtils, Mockito.times(1))
                .passwordHashing(password, user.getUserSalt());
            Mockito.verify(jwtUtils, Mockito.times(0)).parseUserToken(any());
        }
    }

    @Nested
    class parseUserToken {

        @Test
        void 성공_정상적으로_토큰_파싱됨() {
            // give
            String userToken = createTestUserToken();
            UserTokenResponse userTokenResponse = new UserTokenResponse(
                user.getUserId(),
                user.getUserCode(),
                user.getUserImg(),
                user.getUserNickName(),
                user.getUserCreatedAt().toString(),
                user.getUserCreditRank()
            );
            given(jwtUtils.parseUserToken(userToken)).willReturn(userTokenResponse);

            // when
            UserTokenResponse response = tokenService.parseUserToken(userToken);

            // then
            Mockito.verify(jwtUtils, Mockito.times(1)).parseUserToken(userToken);
            assertEquals(user.getUserId(), response.id());
        }
    }
}