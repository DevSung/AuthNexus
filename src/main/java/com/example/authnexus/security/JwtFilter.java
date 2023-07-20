package com.example.authnexus.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // "api/member/login" 로그인 api는 토큰 검증을 수행하지 않고 통과시킴
        if (request.getRequestURI().equals("/api/member/login") || request.getRequestURI().equals("/api/member/join")) {
            chain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().equals("/api/auth/refresh")) {
            Authentication authentication = jwtTokenProvider.checkRefreshToken(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
            return;
        }

        // 2. validateToken 으로 토큰 유효성 검사
        if (jwtTokenProvider.validateToken(request)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);

    }

}
