package com.example.authnexus.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JwtToken {

    /**
     * Jwt 인증 타입
     */
    private String grantType;

    /**
     * Access Token
     */
    private String accessToken;

    /**
     * Refresh Token
     */
    private String refreshToken;

}
