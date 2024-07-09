package com.click.auth.service;

import com.click.auth.domain.dto.request.KakaoTokenRequest;

public interface KakaoAuthService {
    String getUserToken(KakaoTokenRequest req);
}
