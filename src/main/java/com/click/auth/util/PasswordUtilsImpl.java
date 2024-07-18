package com.click.auth.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtilsImpl implements PasswordUtils {

    private final int iterations;

    @Override
    public String passwordHashing(String password, String salt) {
        byte[] digest = null;
        try {
            digest = sha256(password, salt.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
        return byteToHex(digest);
    }

    public byte[] sha256(String rawData, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);
        byte[] result = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < iterations; i++) {
            digest.reset();
            result = digest.digest(result);
        }
        return result;
    }

    @Override
    public String byteToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public PasswordUtilsImpl(
        @Value("${hash.iterations}") int iterations) {
        this.iterations = iterations;
    }
}
