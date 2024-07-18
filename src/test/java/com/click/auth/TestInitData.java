package com.click.auth;

import com.click.auth.domain.entity.User;
import com.click.auth.domain.type.UserIdentityType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class TestInitData {

    protected final User user;
    protected final String publicKey;

    protected String createTestUserToken() {
        return Jwts.builder()
            .claim("id", user.getUserId())
            .claim("code", user.getUserCode())
            .claim("img", user.getUserImg())
            .claim("name", user.getUserNickName())
            .claim("createdAt", user.getUserCreatedAt().toString())
            .claim("rank", user.getUserCreditRank())
            .expiration(new Date(System.currentTimeMillis() + 1000))
            .signWith(Keys.hmacShaKeyFor(publicKey.getBytes()))
            .compact();
    }

    public TestInitData() {
        this.publicKey = "thisispublicKeythisispublicKeythisispublicKeythisispublicKey";
        this.user = new User(
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            "3600000000",
            UserIdentityType.KAKAO,
            "AAAAA",
            "img.png",
            "수진",
            "ce5fd1da07801ff3a9ea4e1a0c9f3d34b83ed0775f8ecc6efe1642baa815587d",
            "31f97641272e9dd6",
            700,
            LocalDateTime.now().minusDays(1),
            10,
            false
        );
    }
}
