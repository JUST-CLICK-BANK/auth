package com.click.auth.service;

import com.click.auth.TestInitData;
import com.click.auth.domain.dto.response.LoginTokenResponse;
import com.click.auth.domain.dto.response.UserTokenResponse;
import com.click.auth.exception.LoginExpirationException;
import com.click.auth.exception.NotFoundExcetion;
import com.click.auth.exception.PasswordMatchException;
import com.click.auth.util.JwtUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest extends TestInitData{
    @InjectMocks
    private TokenServiceImpl tokenService;
    @Mock
    private AuthServiceImpl authService;
    @Mock
    private JwtUtils jwtUtils;

    @Nested
    class generateUserToken {
        @Test
        void 성공_정상적으로_토큰_생성됨() {
            // give
            String accessToken = "QWERASDFZXCVQWERASDFZXCVQWERASDFZXCV";
            String password = "000000";
            LoginTokenResponse loginTokenResponse = new LoginTokenResponse(
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    10
            );
            given(jwtUtils.parseLoginToken(accessToken)).willReturn(loginTokenResponse);
            given(authService.findUserByUuid(loginTokenResponse.uuid())).willReturn(user);
            given(jwtUtils.createUserToken(user)).willReturn("aaaaaaaaaaaaaaaaaaaaaaa");

            // when
            String response = tokenService.generateUserToken(accessToken, password);

            // then
            Mockito.verify(jwtUtils, Mockito.times(1)).parseLoginToken(accessToken);
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(loginTokenResponse.uuid());
            Mockito.verify(jwtUtils, Mockito.times(1)).createUserToken(user);
            assertEquals("aaaaaaaaaaaaaaaaaaaaaaa", response);
        }

        @Test
        void 실패_유저가_존재하지_않음() {
            // give
            String accessToken = "QWERASDFZXCVQWERASDFZXCVQWERASDFZXCV";
            String password = "000000";
            LoginTokenResponse loginTokenResponse = new LoginTokenResponse(
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    10
            );
            given(jwtUtils.parseLoginToken(accessToken)).willReturn(loginTokenResponse);
            given(authService.findUserByUuid(loginTokenResponse.uuid())).willThrow(NotFoundExcetion.class);

            // when
            assertThrows(NotFoundExcetion.class, () -> tokenService.generateUserToken(accessToken, password));

            // then
            Mockito.verify(jwtUtils, Mockito.times(1)).parseLoginToken(accessToken);
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(loginTokenResponse.uuid());
            Mockito.verify(jwtUtils, Mockito.times(0)).createUserToken(user);
        }

        @Test
        void 실패_간편비밀번호가_일치하지_않음() {
            // give
            String accessToken = "QWERASDFZXCVQWERASDFZXCVQWERASDFZXCV";
            String password = "111111";
            LoginTokenResponse loginTokenResponse = new LoginTokenResponse(
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    10
            );
            given(jwtUtils.parseLoginToken(accessToken)).willReturn(loginTokenResponse);
            given(authService.findUserByUuid(loginTokenResponse.uuid())).willReturn(user);

            // when
            assertThrows(PasswordMatchException.class, () -> tokenService.generateUserToken(accessToken, password));

            // then
            Mockito.verify(jwtUtils, Mockito.times(1)).parseLoginToken(accessToken);
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(loginTokenResponse.uuid());
            Mockito.verify(jwtUtils, Mockito.times(0)).createUserToken(user);
        }

        @Test
        void 실패_로그인토큰_버전이_일치하지_않음() {
            // give
            String accessToken = "QWERASDFZXCVQWERASDFZXCVQWERASDFZXCV";
            String password = "000000";
            LoginTokenResponse loginTokenResponse = new LoginTokenResponse(
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    1
            );
            given(jwtUtils.parseLoginToken(accessToken)).willReturn(loginTokenResponse);
            given(authService.findUserByUuid(loginTokenResponse.uuid())).willReturn(user);

            // when
            assertThrows(LoginExpirationException.class, () -> tokenService.generateUserToken(accessToken, password));

            // then
            Mockito.verify(jwtUtils, Mockito.times(1)).parseLoginToken(accessToken);
            Mockito.verify(authService, Mockito.times(1)).findUserByUuid(loginTokenResponse.uuid());
            Mockito.verify(jwtUtils, Mockito.times(0)).createUserToken(user);
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