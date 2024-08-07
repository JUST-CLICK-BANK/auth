package com.click.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.click.auth.TestInitData;
import com.click.auth.api.kakao.KaKaoApi;
import com.click.auth.domain.dao.UserDao;
import com.click.auth.domain.dto.response.KakaoTokenResponse;
import com.click.auth.domain.dto.response.KakaoUserAccount;
import com.click.auth.domain.dto.response.KakaoUserInfoResponse;
import com.click.auth.domain.dto.response.KakaoUserProfile;
import com.click.auth.domain.dto.response.LoginTokenResponse;
import com.click.auth.domain.dto.response.SocialLoginResponse;
import com.click.auth.domain.type.UserIdentityType;
import com.click.auth.exception.NotFoundExcetion;
import com.click.auth.util.JwtUtils;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest extends TestInitData {

    @InjectMocks
    @Spy
    private LoginServiceImpl loginService;
    @Mock
    private UserDao userDao;
    @Mock
    private KaKaoApi kaKaoApi;
    @Mock
    private JwtUtils jwtUtils;

    private final KakaoTokenResponse kakaoToken = new KakaoTokenResponse(
            "bearer",
            "bbbbbbbbbbbbbbbbbbb",
            null,
            null,
            null
    );
    private final KakaoUserProfile kakaoUserProfile = new KakaoUserProfile(
            user.getUserNickName(),
            null,
            user.getUserImg(),
            false,
            false
    );
    private final KakaoUserAccount kakaoUserAccount = new KakaoUserAccount(
            true,
            kakaoUserProfile
    );
    private final KakaoUserInfoResponse kakaoUserInfo = new KakaoUserInfoResponse(
            3600000000L,
            null,
            kakaoUserAccount
    );

    @Nested
    class generateLoginToken {

        @Test
        void 성공_정상적으로_로그인토큰_생성됨_이미지는_없어서_변경되지_않음() {
            // give
            String identity = "3600000000";
            UserIdentityType type = UserIdentityType.KAKAO;
            String image = null;
            given(userDao.selectOptionalUser(identity, type)).willReturn(Optional.of(user));

            // when
            loginService.generateLoginToken(identity, type, image);

            // then
            Mockito.verify(userDao, Mockito.times(1)).selectOptionalUser(identity, type);
            Mockito.verify(userDao, Mockito.times(0)).updateUserImage(user.getUserId(), image);
            Mockito.verify(jwtUtils, Mockito.times(1))
                    .createLoginToken(LoginTokenResponse.from(user));
        }

        @Test
        void 성공_정상적으로_로그인토큰_생성됨_이미지가_같아서_변경되지_않음() {
            // give
            String identity = "3600000000";
            UserIdentityType type = UserIdentityType.KAKAO;
            String image = "img.png";
            given(userDao.selectOptionalUser(identity, type)).willReturn(Optional.of(user));

            // when
            loginService.generateLoginToken(identity, type, image);

            // then
            Mockito.verify(userDao, Mockito.times(1)).selectOptionalUser(identity, type);
            Mockito.verify(userDao, Mockito.times(0)).updateUserImage(user.getUserId(), image);
            Mockito.verify(jwtUtils, Mockito.times(1))
                    .createLoginToken(LoginTokenResponse.from(user));
        }

        @Test
        void 성공_정상적으로_로그인토큰_생성하고_이미지도_변경됨() {
            // give
            String identity = "3600000000";
            UserIdentityType type = UserIdentityType.KAKAO;
            String image = "changed.png";
            given(userDao.selectOptionalUser(identity, type)).willReturn(Optional.of(user));

            // when
            loginService.generateLoginToken(identity, type, image);

            // then
            Mockito.verify(userDao, Mockito.times(1)).selectOptionalUser(identity, type);
            Mockito.verify(userDao, Mockito.times(1)).updateUserImage(user.getUserId(), image);
            Mockito.verify(jwtUtils, Mockito.times(1))
                    .createLoginToken(LoginTokenResponse.from(user));
        }

        @Test
        void 실패_유저를_찾지못해_예외_던짐() {
            // give
            String identity = "3611111111";
            UserIdentityType type = UserIdentityType.KAKAO;
            String image = "newimage.png";
            given(userDao.selectOptionalUser(identity, type)).willReturn(Optional.empty());

            // when
            assertThrows(NotFoundExcetion.class,
                    () -> loginService.generateLoginToken(identity, type, image));

            // then
            Mockito.verify(userDao, Mockito.times(1)).selectOptionalUser(identity, type);
            Mockito.verify(userDao, Mockito.times(0)).updateUserImage(user.getUserId(), image);
            Mockito.verify(jwtUtils, Mockito.times(0))
                    .createLoginToken(LoginTokenResponse.from(user));
        }
    }

    @Nested
    class getUserTokenByKakao {

        @Test
        void 성공_정상적으로_카카오_토큰_정보_받아옴_이미_가입된_유저() {
            // give
            String authCode = "aaaaaaaaaaaaaaaaaaaaaa";
            given(loginService.getKakaoToken(authCode)).willReturn(kakaoToken);
            given(loginService.getKakaoUserInfo(kakaoToken.access_token())).willReturn(
                    kakaoUserInfo);
            given(userDao.selectOptionalUser(kakaoUserInfo.id().toString(),
                    UserIdentityType.KAKAO)).willReturn(Optional.of(user));

            // when
            SocialLoginResponse response = loginService.getUserTokenByKakao(authCode);

            // then
            Mockito.verify(loginService, Mockito.times(1))
                    .getKakaoToken(authCode);
            Mockito.verify(loginService, Mockito.times(1))
                    .getKakaoUserInfo(kakaoToken.access_token());
            Mockito.verify(userDao, Mockito.times(1))
                    .selectOptionalUser(kakaoUserInfo.id().toString(), UserIdentityType.KAKAO);
            assertEquals(kakaoUserInfo.id().toString(), response.identity());
            assertEquals(true, response.isAlready());
        }

        @Test
        void 성공_정상적으로_카카오_토큰_정보_받아옴_가입되지_않은_유저() {
            // give
            String authCode = "aaaaaaaaaaaaaaaaaaaaaa";
            given(loginService.getKakaoToken(authCode)).willReturn(kakaoToken);
            given(loginService.getKakaoUserInfo(kakaoToken.access_token())).willReturn(
                    kakaoUserInfo);
            given(userDao.selectOptionalUser(kakaoUserInfo.id().toString(),
                    UserIdentityType.KAKAO)).willReturn(Optional.empty());

            // when
            SocialLoginResponse response = loginService.getUserTokenByKakao(authCode);

            // then
            Mockito.verify(loginService, Mockito.times(1))
                    .getKakaoToken(authCode);
            Mockito.verify(loginService, Mockito.times(1))
                    .getKakaoUserInfo(kakaoToken.access_token());
            Mockito.verify(userDao, Mockito.times(1))
                    .selectOptionalUser(kakaoUserInfo.id().toString(), UserIdentityType.KAKAO);
            assertEquals(kakaoUserInfo.id().toString(), response.identity());
            assertEquals(false, response.isAlready());
        }
    }

    @Nested
    class getKakaoToken {

        @Test
        void 성공_카카오에서_응답_받음() {
            // give
            String authCode = "aaaaaaaaaaaaaaaaaaaaaa";
            given(kaKaoApi.getKakaoToken(authCode)).willReturn(kakaoToken);

            // when
            KakaoTokenResponse response = loginService.getKakaoToken(authCode);

            // then
            Mockito.verify(kaKaoApi, Mockito.times(1)).getKakaoToken(authCode);
            assertEquals(kakaoToken.access_token(), response.access_token());
        }
    }

    @Nested
    class getKakaoUserInfo {

        @Test
        void 성공_카카오에서_응답_받음() {
            // give
            String token = "bbbbbbbbbbbbbbbbbbbbbb";
            given(kaKaoApi.getKakaoUserInfo(token)).willReturn(kakaoUserInfo);

            // when
            KakaoUserInfoResponse response = loginService.getKakaoUserInfo(token);

            // then
            Mockito.verify(kaKaoApi, Mockito.times(1)).getKakaoUserInfo(token);
            assertEquals(kakaoUserInfo.id(), response.id());
        }
    }
}