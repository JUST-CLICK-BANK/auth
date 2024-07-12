package com.click.auth.controller;

import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.domain.dto.response.UserResponse;
import com.click.auth.domain.dto.response.UserTokenResponse;
import com.click.auth.domain.type.UserIdentityType;
import com.click.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/version")
    public String getVersion() {
        return "1.0v";
    }

    @PostMapping
    public String createUser(@RequestBody UserCreateRequest req) {
        return authService.createUser(req);
    }

    @PutMapping("/{id}")
    public void updateUser(
            @PathVariable("id") UUID id,
            @RequestParam(value = "image", required = false) String image,
            @RequestParam(value = "nickname", required = false) String name
    ){
        authService.updateUserProfile(id, image, name);
    }

    @GetMapping("/{code}")
    public UserResponse findFriend(@PathVariable("code") String code) {
        return authService.findUserByCode(code);
    }

    @GetMapping("/login/token")
    public String getLoginToken(
            @RequestParam("identity") String identity,
            @RequestParam("type") UserIdentityType type
    ){
        return authService.generateLoginToken(identity, type);
    }

    @GetMapping("/token")
    public String getUserToken(
            @RequestParam("token") String token,
            @RequestParam("password") String password
    ){
        return authService.generateUserToken(token, password);
    }

    @GetMapping("/token/{userToken}")
    public UserTokenResponse getUserTokenInfo(@PathVariable("userToken") String userToken) {
        return authService.parseUserToken(userToken);
    }
}
