package com.click.auth.util;

import com.click.auth.domain.dto.LoginTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    private final SecretKey secretKey;

    public String createLoginToken(LoginTokenDto loginTokenDto) {
        return Jwts.builder()
                .claim("uuid", loginTokenDto.uuid())
                .claim("password", loginTokenDto.password())
                .claim("version", loginTokenDto.version())
                .expiration(new Date(System.currentTimeMillis() + 1814400000))
                .signWith(secretKey)
                .compact();
    }

    public LoginTokenDto parseLoginToken(String loginToken) {
        Claims claims = (Claims) Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parse(loginToken)
                .getPayload();
        return LoginTokenDto.from(claims);
    }

    public JwtUtils(@Value("${jwt.secret_key}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }
}
