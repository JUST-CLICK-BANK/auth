package com.click.auth.api.kakao;

import com.click.auth.domain.dto.response.KakaoTokenInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "KakaoApi", url = "https://kapi.kakao.com")
public interface KakaoApiFeign {

    @GetMapping("/v1/user/access_token_info")
    KakaoTokenInfoResponse getKakaoTokenInfo(@RequestHeader("Authorization") String token);
}
