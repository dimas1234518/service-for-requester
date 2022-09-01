package com.zhevakin.service_for_requester.models.domain;


import lombok.Getter;

@Getter
public class JwtResponse {

    private final String type = "Bearer";
    private final String accessToken;
    private final String refreshToken;

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
