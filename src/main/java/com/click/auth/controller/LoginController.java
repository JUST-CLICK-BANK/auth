package com.click.auth.controller;

import com.click.auth.domain.dto.response.SocialLoginResponse;
import com.click.auth.domain.type.UserIdentityType;
import com.click.auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/login")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login/token")
    public String getLoginToken(
            @RequestParam("identity") String identity,
            @RequestParam("type") UserIdentityType type
    ){
        return loginService.generateLoginToken(identity, type);
    }

    @GetMapping("/kakao")
    public SocialLoginResponse getUserTokenByKakao(
            @RequestParam(value = "code") String kakaoCode,
            @RequestParam(value = "isFront", defaultValue = "false") Boolean isFront
    ){
        if (isFront) {
            return loginService.getUserTokenByKakao(kakaoCode);
        }
        return null;
    }
}
