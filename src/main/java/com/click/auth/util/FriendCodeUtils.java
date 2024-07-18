package com.click.auth.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class FriendCodeUtils {

    private final String codeSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String generateCode() {
        StringBuilder code = new StringBuilder();
        while (code.length() < 5) {
            code.append(codeSet.charAt(new Random().nextInt(36)));
        }
        return code.toString();
    }
}
