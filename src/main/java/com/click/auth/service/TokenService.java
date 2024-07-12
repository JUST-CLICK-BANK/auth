package com.click.auth.service;

import com.click.auth.domain.dto.response.UserTokenResponse;

public interface TokenService {
    String generateUserToken(String accessToken, String password);
    UserTokenResponse parseUserToken(String userToken);
}
