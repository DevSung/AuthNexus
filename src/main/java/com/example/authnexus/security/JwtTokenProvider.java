package com.example.authnexus.security;

import com.example.authnexus.domain.common.RefreshToken;
import com.example.authnexus.domain.common.repository.RefreshTokenRepository;
import com.example.authnexus.domain.member.Member;
import com.example.authnexus.domain.member.MemberRole;
import com.example.authnexus.domain.member.repository.MemberRepository;
import com.example.authnexus.exception.ApiResponseException;
import com.example.authnexus.exception.ExceptionCode;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * JWT Token 생성
     */
    public JwtToken generateToken(Member member) {
        String authorities = member.getRoles().stream().map(MemberRole::getRole).collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(member.getIdx()))
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .setSubject(String.valueOf(member.getIdx()))
                .claim("auth", authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        addRefreshToken(member, refreshToken);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = resolveToken(request);

        // 토큰 복호화
        Claims claims = parseClaims(token);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = getAuthorities(claims);

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(HttpServletRequest request) {
        String token = resolveToken(request);
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

    /**
     * 리프레쉬 토큰 저장
     */
    private void addRefreshToken(Member member, String refreshToken) {
        if (refreshTokenRepository.existsByMemberIdx(member.getIdx())) {
            refreshTokenRepository.deleteByMemberIdx(member.getIdx());
        }
        RefreshToken token = RefreshToken.builder()
                .memberIdx(member.getIdx())
                .token(refreshToken)
                .build();

        refreshTokenRepository.save(token);
    }

    /**
     * 리프레쉬 토큰 검증
     */
    public Authentication checkRefreshToken(HttpServletRequest request) {
        String token = resolveToken(request);

        // 토큰 복호화
        Claims claims = parseClaims(token);

        if (!refreshTokenRepository.existsByMemberIdxAndToken(Long.valueOf(claims.getSubject()), token)) {
            throw new ApiResponseException(ExceptionCode.ERROR_ACCESS_DENIED, "유효하지 않은 토큰입니다.");
        }

        Optional<Member> member = memberRepository.findById(Long.valueOf(claims.getSubject()));

        if (member.isEmpty()) {
            throw new ApiResponseException(ExceptionCode.ERROR_NOT_FOUND, "회원 정보가 존재하지 않습니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = getAuthorities(claims);

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", getAuthorities(claims));

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * Request Header 에서 토큰 정보 추출
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 클레임에서 권한 정보 가져오기
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Claims claims) {
        return Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * 토큰에서 사용자 idx 추출
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

}
