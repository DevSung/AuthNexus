package com.example.authnexus.security;

import com.example.authnexus.domain.common.RefreshToken;
import com.example.authnexus.domain.common.repository.RefreshTokenRepository;
import com.example.authnexus.domain.member.Member;
import com.example.authnexus.domain.member.MemberRole;
import com.example.authnexus.payload.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final RefreshTokenRepository refreshTokenRepository;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // JWT Token 생성
    public JwtToken generateToken(Member member) {
        String authorities = member.getRoles().stream().map(MemberRole::getRole).collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(member.getUserId())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .setSubject(member.getUserId())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        addRefreshToken(member, refreshToken);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = parseClaims(token);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // refresh token db insert
    private void addRefreshToken(Member member, String refreshToken) {
        if (refreshTokenRepository.existsByUserIdAndToken(member.getUserId(), refreshToken)) {
            refreshTokenRepository.deleteByUserIdAndToken(member.getUserId(), refreshToken);
        }
        RefreshToken token = RefreshToken.builder()
                .userId(member.getUserId())
                .token(refreshToken)
                .build();

        refreshTokenRepository.save(token);
    }

}
