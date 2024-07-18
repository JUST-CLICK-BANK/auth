package com.click.auth.api.kakao;

import com.click.auth.domain.dto.response.KakaoTokenInfoResponse;
import com.click.auth.domain.dto.response.KakaoTokenResponse;
import com.click.auth.domain.dto.response.KakaoUserInfoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KaKaoApi {

    private final KakaoAuthFeign kakaoAuthFeign;
    private final KakaoApiFeign kakaoApiFeign;

    @Value("${kakao.client_id}")
    private String client_id;
    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    /*
    public KakaoTokenResponse getKakaoToken(String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE,
            "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("redirect_uri", redirect_uri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params,
            httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            httpEntity,
            KakaoTokenResponse.class
        );
        return response.getBody();
    }
    */

    public KakaoTokenResponse getKakaoToken(String code) {
        Map<String, String> form = new HashMap<>();
        form.put("grant_type", "authorization_code");
        form.put("client_id", client_id);
        form.put("redirect_uri", redirect_uri);
        form.put("code", code);
        return kakaoAuthFeign.getKakaoToken(form);
    }

    public KakaoTokenInfoResponse getKakaoTokenInfo(KakaoTokenResponse req) {
        return kakaoApiFeign.getKakaoTokenInfo("Bearer " + req.access_token());
    }

    public KakaoUserInfoResponse getKakaoUserInfo(String token) {
        List<String> env = List.of("kakao_account.profile");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonArray = objectMapper.writeValueAsString(env);
            return kakaoApiFeign.getKakaoUserInfo("Bearer " + token, jsonArray);
        } catch (Exception e) {
            return null;
        }
    }
}
