package com.example.authnexus.controller;

import com.example.authnexus.service.MemberService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("local")
class AuthControllerTest {

    @Autowired
    MemberService authService;

    @Test
    @DisplayName("토큰 검증 테스트")
    void createToken() {
        Claims claims = Jwts.claims();
        claims.setSubject("sungsin"); // 식별자

        Date iat = new Date();
        claims.setIssuedAt(iat); // 발급시간

        Date exp = new Date(iat.getTime() + 1000 * 60 * 60 * 24);
        claims.setExpiration(exp); // 만료시간

        String random256BitKey = "6v9y$B&E)H@MbQeThWmZq4t7w!z%C*F-";

        SecretKey secretKey = Keys.hmacShaKeyFor(random256BitKey.getBytes());

        JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey).build();

        String jws = Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        Jws<Claims> claimsJws = parser.parseClaimsJws(jws);

        assertThat(claimsJws.getBody().getSubject()).isEqualTo("sungsin");
        assertThat(claimsJws.getBody().getExpiration().before(iat)).isFalse();
    }


}