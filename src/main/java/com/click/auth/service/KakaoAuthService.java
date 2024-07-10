package com.click.auth.service;

import com.click.auth.domain.dto.response.SocialLoginResponse;

public interface KakaoAuthService {
    SocialLoginResponse getUserToken(String AuthCode);
}
