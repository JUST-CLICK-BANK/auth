package com.click.auth.controller;

import com.click.auth.api.KaKaoApi;
import com.click.auth.domain.dto.request.KakaoTokenRequest;
import com.click.auth.domain.dto.response.UserTokenResponse;
import com.click.auth.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {
    private final KakaoAuthService kakaoAuthService;

    @PostMapping
    public String getKakaoToken(@RequestBody KakaoTokenRequest req) {
        return kakaoAuthService.getUserToken(req);
    }
}
