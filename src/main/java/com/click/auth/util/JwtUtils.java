package com.click.auth.util;

import com.click.auth.domain.dto.response.LoginTokenResponse;
import com.click.auth.domain.dto.response.UserTokenResponse;
import com.click.auth.domain.entity.User;
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
    private final SecretKey publicKey;

    public String createLoginToken(LoginTokenResponse loginTokenResponse) {
        return Jwts.builder()
                .claim("uuid", loginTokenResponse.uuid())
                .claim("version", loginTokenResponse.version())
                .expiration(new Date(System.currentTimeMillis() + 1814400000))
                .signWith(secretKey)
                .compact();
    }

    public String createUserToken(User user) {
        return Jwts.builder()
                .claim("id", user.getUserId())
                .claim("code", user.getUserCode())
                .claim("img", user.getUserImg())
                .claim("name", user.getUserNickName())
                .claim("createdAt", user.getUserCreatedAt().toString())
                .claim("rank", user.getUserCreditRank())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(publicKey)
                .compact();
    }

    public LoginTokenResponse parseLoginToken(String loginToken) {
        Claims claims = (Claims) Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parse(loginToken)
                .getPayload();
        return LoginTokenResponse.from(claims);
    }

    public UserTokenResponse parseUserToken(String userToken) {
        Claims claims = (Claims) Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parse(userToken)
                .getPayload();
        return UserTokenResponse.from(claims);
    }

    public JwtUtils(
            @Value("${jwt.secret_key}") String secret,
            @Value("${jwt.public_key}") String pub) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.publicKey = Keys.hmacShaKeyFor(pub.getBytes());
    }
}
