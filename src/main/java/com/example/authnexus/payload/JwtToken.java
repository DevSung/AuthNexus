package com.example.authnexus.payload;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
