package com.click.auth.service;

import com.click.auth.api.kakao.KaKaoApi;
import com.click.auth.domain.dto.response.KakaoTokenInfoResponse;
import com.click.auth.domain.dto.response.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAuthServiceImpl implements KakaoAuthService{
    private final KaKaoApi kaKaoApi;

    @Override
    public UserCreateInitDataResponse getUserToken(String authCode) {
        KakaoTokenResponse kakaoToken = getToken(authCode);
        KakaoTokenInfoResponse kakaoTokenInfo = getTokenInfo(kakaoToken);
        return UserCreateInitDataResponse.fromKakao(kakaoTokenInfo);
    }

    public KakaoTokenResponse getToken(String authCode) {
        return kaKaoApi.getKakaoToken(authCode);
    }

    public KakaoTokenInfoResponse getTokenInfo(KakaoTokenResponse token) {
        return kaKaoApi.getKakaoTokenInfo(token);
    }

}
