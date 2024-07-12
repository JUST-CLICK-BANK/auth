package com.click.auth.controller;

import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.domain.dto.response.UserResponse;
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

    @GetMapping("/{code}")
    public UserResponse findFriend(@PathVariable("code") String code) {
        return authService.findUserByCode(code);
    }

    @PutMapping("/{id}")
    public void updateUser(
            @PathVariable("id") UUID id,
            @RequestParam(value = "image", required = false) String image,
            @RequestParam(value = "nickname", required = false) String name
    ){
        authService.updateUserProfile(id, image, name);
    }

    @DeleteMapping("/{id}")
    public void deleteUSer(@PathVariable("id") UUID id) {
        authService.disableUser(id);
    }

}
