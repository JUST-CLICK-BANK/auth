package com.click.auth.service;

import com.click.auth.api.KaKaoApi;
import com.click.auth.domain.dto.request.KakaoTokenRequest;
import com.click.auth.domain.dto.response.KakaoTokenInfoResponse;
import com.click.auth.domain.dto.response.KakaoTokenResponse;
import com.click.auth.domain.dto.response.UserTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoAuthServiceImpl implements KakaoAuthService{
    private final KaKaoApi kaKaoApi;

    @Override
    public String getUserToken(KakaoTokenRequest req) {
        KakaoTokenResponse kakaoToken = getToken(req);
        KakaoTokenInfoResponse kakaoTokenInfo = getTokenInfo(kakaoToken);
        return null;
    }

    public KakaoTokenResponse getToken(KakaoTokenRequest req) {
        return kaKaoApi.getKakaoToken(req);
    }

    public KakaoTokenInfoResponse getTokenInfo(KakaoTokenResponse token) {
        return kaKaoApi.getKakaoTokenInfo(token);
    }

}
