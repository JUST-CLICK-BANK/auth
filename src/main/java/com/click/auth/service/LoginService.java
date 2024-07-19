package com.click.auth.service;

import com.click.auth.domain.dto.response.SocialLoginResponse;
import com.click.auth.domain.type.UserIdentityType;

public interface LoginService {

    String generateLoginToken(String identity, UserIdentityType type, String image);

    SocialLoginResponse getUserTokenByKakao(String authCode);
}
