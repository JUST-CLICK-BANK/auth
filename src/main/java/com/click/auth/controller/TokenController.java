package com.click.auth.controller;

import com.click.auth.domain.dto.response.UserTokenResponse;
import com.click.auth.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    // 유저 토큰 발급
    @GetMapping
    public String getUserToken(
        @RequestParam("token") String token,
        @RequestParam("password") String password
    ) {
        return tokenService.generateUserToken(token, password);
    }

    // 유저 토큰 파싱
    @GetMapping("/{userToken}")
    public UserTokenResponse getUserTokenInfo(@PathVariable("userToken") String userToken) {
        return tokenService.parseUserToken(userToken);
    }
}
