package com.click.auth.controller;

import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.domain.dto.request.UserUpdateRequest;
import com.click.auth.domain.dto.response.UserListResponse;
import com.click.auth.domain.dto.response.UserResponse;
import com.click.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping
    public UserResponse findFriend(@RequestParam("code") String code) {
        return authService.findUserByCode(code);
    }

    @GetMapping("/friends")
    public List<UserListResponse> findFriendInfoList(@RequestParam("codes") String[] codes){
        return authService.findUsersByCodes(codes);
    }

    @PutMapping("/{id}/image")
    public void updateUserImage(
            @PathVariable("id") UUID id,
            @RequestBody UserUpdateRequest req
    ) {
        authService.updateUserImage(id, req.data());
    }

    @PutMapping("/{id}/nickname")
    public void updateUserNickname(
            @PathVariable("id") UUID id,
            @RequestBody UserUpdateRequest req
    ){
        authService.updateUserNickname(id, req.data());
    }

    @PutMapping("/{id}/token")
    public void updateTokenVersion(@PathVariable("id") UUID id){
        authService.updateTokenVersion(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUSer(@PathVariable("id") UUID id) {
        authService.disableUser(id);
    }

}
