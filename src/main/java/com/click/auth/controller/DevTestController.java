package com.click.auth.controller;

import com.click.auth.util.FriendCodeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/test")
@RequiredArgsConstructor
public class DevTestController {
    private final FriendCodeUtils friendCodeUtils;

    @GetMapping
    public String getCode(){
        return friendCodeUtils.generateCode();
    }
}
