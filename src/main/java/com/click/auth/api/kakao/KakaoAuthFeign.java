package com.click.auth.api.kakao;

import com.click.auth.domain.dto.response.KakaoTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "KakaoAuth", url = "https://kauth.kakao.com", configuration = KakaoAuthFeignConfig.class)
public interface KakaoAuthFeign {

    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    KakaoTokenResponse getKakaoToken(@RequestBody Map<String, ?> form);
}
