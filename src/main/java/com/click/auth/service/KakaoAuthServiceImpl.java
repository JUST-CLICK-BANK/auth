package com.click.auth.service;

import com.click.auth.api.kakao.KaKaoApi;
import com.click.auth.domain.dto.response.KakaoTokenInfoResponse;
import com.click.auth.domain.dto.response.KakaoTokenResponse;
import com.click.auth.domain.dto.response.SocialLoginResponse;
import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;
import com.click.auth.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAuthServiceImpl implements KakaoAuthService{
    private final KaKaoApi kaKaoApi;
    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @Override
    public SocialLoginResponse getUserToken(String authCode) {
        KakaoTokenResponse kakaoToken = getToken(authCode);
        KakaoTokenInfoResponse kakaoTokenInfo = getTokenInfo(kakaoToken);
        User userByIdentity = authService.findUserByIdentity(kakaoTokenInfo.id().toString(), UserIdentityType.KAKAO);
        if (userByIdentity == null) {
            return SocialLoginResponse.fromKakao(kakaoTokenInfo, false);
        }
        return SocialLoginResponse.fromKakao(kakaoTokenInfo, true);
    }

    public KakaoTokenResponse getToken(String authCode) {
        return kaKaoApi.getKakaoToken(authCode);
    }

    public KakaoTokenInfoResponse getTokenInfo(KakaoTokenResponse token) {
        return kaKaoApi.getKakaoTokenInfo(token);
    }

}
