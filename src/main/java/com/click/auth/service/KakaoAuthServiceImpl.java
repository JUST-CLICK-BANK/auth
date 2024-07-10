package com.click.auth.service;

import com.click.auth.api.kakao.KaKaoApi;
import com.click.auth.domain.dto.request.KakaoTokenRequest;
import com.click.auth.domain.dto.response.KakaoTokenInfoResponse;
import com.click.auth.domain.dto.response.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAuthServiceImpl implements KakaoAuthService{
    private final KaKaoApi kaKaoApi;

    @Override
    public String getUserToken(String authCode) {
        KakaoTokenResponse kakaoToken = getToken(authCode);
        KakaoTokenInfoResponse kakaoTokenInfo = getTokenInfo(kakaoToken);
        return kakaoTokenInfo.id().toString();
    }

    public KakaoTokenResponse getToken(String authCode) {
        return kaKaoApi.getKakaoToken(authCode);
    }

    public KakaoTokenInfoResponse getTokenInfo(KakaoTokenResponse token) {
        return kaKaoApi.getKakaoTokenInfo(token);
    }

}
