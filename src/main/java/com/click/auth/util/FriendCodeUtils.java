package com.click.auth.util;

import java.util.Random;

public class FriendCodeUtils {

    private static final String codeSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generateCode() {
        StringBuilder code = new StringBuilder();
        while (code.length() < 5) {
            code.append(codeSet.charAt(new Random().nextInt(36)));
        }
        return code.toString();
    }
}
