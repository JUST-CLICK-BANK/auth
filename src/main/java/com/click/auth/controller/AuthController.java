package com.click.auth.controller;

import com.click.auth.domain.dto.request.UserCreateRequest;
import com.click.auth.domain.dto.request.UserUpdateRequest;
import com.click.auth.domain.dto.response.UserListResponse;
import com.click.auth.domain.dto.response.UserResponse;
import com.click.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 서버 버전 확인
    @GetMapping("/version")
    public String getVersion() {
        return "v1.1.2";
    }

    // 유저 생성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@RequestBody UserCreateRequest req) {
        return authService.createUser(req);
    }

    // 친구 유저 정보
    @GetMapping
    public UserResponse findFriend(@RequestParam("code") String code) {
        return authService.findUserByCode(code);
    }

    // 친구들 유저 정보
    @GetMapping("/friends")
    public List<UserListResponse> findFriendInfoList(@RequestParam("codes") String[] codes) {
        return authService.findUsersByCodes(codes);
    }

    // 대표계좌 설정
    @PutMapping("/{id}/main-account")
    public void updateMainAccount(
        @PathVariable("id") UUID id,
        @RequestBody UserUpdateRequest req
    ) {
        authService.updateMainAccount(id, req.data());
    }

    // 프로필 사진 갱신
    @PutMapping("/{id}/image")
    public void updateUserImage(
        @PathVariable("id") UUID id,
        @RequestBody UserUpdateRequest req
    ) {
        authService.updateUserImage(id, req.data());
    }

    // 닉네임 변경
    @PutMapping("/{id}/nickname")
    public void updateUserNickname(
        @PathVariable("id") UUID id,
        @RequestBody UserUpdateRequest req
    ) {
        authService.updateUserNickname(id, req.data());
    }

    // 비밀번호 변경
    @PutMapping("/{id}/password")
    public void changePassword(
        @PathVariable("id") UUID id,
        @RequestBody UserUpdateRequest req
    ) {
        authService.updateUserPassword(id, req.data());
    }

    // 로그인 토큰 버전 갱싱
    @PutMapping("/{id}/token")
    public void updateTokenVersion(@PathVariable("id") UUID id) {
        authService.updateTokenVersion(id);
    }

    // 유저 비활성화
    @DeleteMapping("/{id}")
    public void deleteUSer(@PathVariable("id") UUID id) {
        authService.disableUser(id);
    }

}
