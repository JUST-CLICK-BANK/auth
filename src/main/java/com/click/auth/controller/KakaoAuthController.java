package com.click.auth.controller;

import com.click.auth.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/kakao")
@RequiredArgsConstructor
public class KakaoAuthController {
    private final KakaoAuthService kakaoAuthService;

    @GetMapping
    public String getUserTokenByKakao(
            @RequestParam(value = "code") String kakaoCode,
            @RequestParam(value = "isFront", defaultValue = "false") Boolean isFront
    ){
        if (isFront) {
            return kakaoAuthService.getUserToken(kakaoCode);
        }
        return null;
    }
}
