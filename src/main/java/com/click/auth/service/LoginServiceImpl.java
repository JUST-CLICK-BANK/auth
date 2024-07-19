package com.click.auth.service;

import com.click.auth.api.kakao.KaKaoApi;
import com.click.auth.domain.dto.response.*;
import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;
import com.click.auth.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
    private final AuthService authService;
    private final KaKaoApi kaKaoApi;
    private final JwtUtils jwtUtils;

    @Override
    public String generateLoginToken(String identity, UserIdentityType type, String image) {
        User user = authService.findUserByIdentity(identity, type);
        if (image != null && !image.equals(user.getUserImg())) {
            authService.updateUserImage(user.getUserId(), image);
        }
        return jwtUtils.createLoginToken(LoginTokenResponse.from(user));
    }

    @Override
    public SocialLoginResponse getUserTokenByKakao(String authCode) {
        KakaoTokenResponse kakaoToken = getKakaoToken(authCode);
//        KakaoTokenInfoResponse kakaoTokenInfo = getKakaoTokenInfo(kakaoToken);
//        User userByIdentity = authService.findUserByIdentity(kakaoTokenInfo.id().toString(), UserIdentityType.KAKAO);
        KakaoUserInfoResponse kakaoUserInfoResponse = getKakaoUserInfo(kakaoToken.access_token());
        User userByIdentity = authService.findUserByIdentity(kakaoUserInfoResponse.id().toString(), UserIdentityType.KAKAO);
        if (userByIdentity == null) {
            return SocialLoginResponse.from(kakaoUserInfoResponse, false);
        }
        return SocialLoginResponse.from(kakaoUserInfoResponse, true);
    }

    public KakaoTokenResponse getKakaoToken(String authCode) {
        return kaKaoApi.getKakaoToken(authCode);
    }

    public KakaoTokenInfoResponse getKakaoTokenInfo(KakaoTokenResponse token) {
        return kaKaoApi.getKakaoTokenInfo(token);
    }

    public KakaoUserInfoResponse getKakaoUserInfo(String token) {
        return kaKaoApi.getKakaoUserInfo(token);
    }
}
