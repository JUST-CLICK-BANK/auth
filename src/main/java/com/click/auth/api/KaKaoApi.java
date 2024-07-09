package com.click.auth.api;

import com.click.auth.domain.dto.request.KakaoTokenRequest;
import com.click.auth.domain.dto.response.KakaoTokenInfoResponse;
import com.click.auth.domain.dto.response.KakaoTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KaKaoApi {
    private final KakaoFeign kakaoFeign;

    public KakaoTokenResponse getKakaoToken(KakaoTokenRequest req) {
        return kakaoFeign.getKakaoToken(req);
    }

    public KakaoTokenInfoResponse getKakaoTokenInfo(KakaoTokenResponse req){
        return kakaoFeign.getKakaoTokenInfo(req.access_token());
    }
}
