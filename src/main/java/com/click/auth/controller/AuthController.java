package com.click.auth.controller;

import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
