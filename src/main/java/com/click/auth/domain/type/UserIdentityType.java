package com.click.auth.domain.type;

public enum UserIdentityType {
    KAKAO("kakao"),
    NAVER("naver");

    private final String type;

    UserIdentityType(String type){
        this.type = type;
    }
}
