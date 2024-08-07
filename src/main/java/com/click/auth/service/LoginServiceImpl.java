package com.click.auth.service;

import com.click.auth.api.kakao.KaKaoApi;
import com.click.auth.domain.dao.UserDao;
import com.click.auth.domain.dto.response.*;
import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;
import com.click.auth.exception.NotFoundExcetion;
import com.click.auth.util.JwtUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserDao userDao;
    private final KaKaoApi kaKaoApi;
    private final JwtUtils jwtUtils;

    @Override
    public String generateLoginToken(String identity, UserIdentityType type, String image) {
        User user = userDao.selectOptionalUser(identity, type)
            .orElseThrow(() -> new NotFoundExcetion("USER"));
        if (image != null && !image.equals(user.getUserImg())) {
            userDao.updateUserImage(user.getUserId(), image);
        }
        return jwtUtils.createLoginToken(LoginTokenResponse.from(user));
    }

    @Override
    public SocialLoginResponse getUserTokenByKakao(String authCode) {
        KakaoTokenResponse kakaoToken = getKakaoToken(authCode);
        KakaoUserInfoResponse kakaoUserInfoResponse = getKakaoUserInfo(kakaoToken.access_token());
        User user = userDao.selectOptionalUser(kakaoUserInfoResponse.id().toString(),
            UserIdentityType.KAKAO).orElse(null);
        if(user == null) {
            return SocialLoginResponse.from(kakaoUserInfoResponse, false);
        }
        return SocialLoginResponse.from(kakaoUserInfoResponse, true);
    }

    public KakaoTokenResponse getKakaoToken(String authCode) {
        return kaKaoApi.getKakaoToken(authCode);
    }

    public KakaoUserInfoResponse getKakaoUserInfo(String token) {
        return kaKaoApi.getKakaoUserInfo(token);
    }
}
