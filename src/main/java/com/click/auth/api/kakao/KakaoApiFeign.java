package com.click.auth.api.kakao;

import com.click.auth.domain.dto.response.KakaoTokenInfoResponse;
import com.click.auth.domain.dto.response.KakaoUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "KakaoApi", url = "https://kapi.kakao.com", configuration = KakaoAuthFeignConfig.class)
public interface KakaoApiFeign {

    @GetMapping("/v1/user/access_token_info")
    KakaoTokenInfoResponse getKakaoTokenInfo(@RequestHeader("Authorization") String token);

    @PostMapping(value = "/v2/user/me", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    KakaoUserInfoResponse getKakaoUserInfo(
            @RequestHeader("Authorization") String token,
            @RequestParam("property_keys") String property_keys
    );
}
