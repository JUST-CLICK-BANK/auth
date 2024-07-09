package com.click.auth.api;


import com.click.auth.domain.dto.request.KakaoTokenRequest;
import com.click.auth.domain.dto.response.KakaoTokenInfoResponse;
import com.click.auth.domain.dto.response.KakaoTokenResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoApi")
public interface KakaoFeign {

    @PostMapping("https://kauth.kakao.com/oauth/token")
    @Headers("Content-type: application/x-www-form-urlencoded;charset=utf-8")
    KakaoTokenResponse getKakaoToken(KakaoTokenRequest req);

    @GetMapping("https://kapi.kakao.com/v1/user/access_token_info")
    KakaoTokenInfoResponse getKakaoTokenInfo(@RequestHeader("Authorization") String token);
}
