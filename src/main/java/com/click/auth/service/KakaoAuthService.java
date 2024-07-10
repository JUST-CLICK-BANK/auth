package com.click.auth.service;

import com.click.auth.domain.dto.response.UserCreateInitDataResponse;

public interface KakaoAuthService {
    UserCreateInitDataResponse getUserToken(String AuthCode);
}
