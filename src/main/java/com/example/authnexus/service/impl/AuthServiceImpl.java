package com.example.authnexus.service.impl;

import com.example.authnexus.domain.entity.Member;
import com.example.authnexus.domain.repository.MemberRepository;
import com.example.authnexus.exception.ApiResponseException;
import com.example.authnexus.exception.ExceptionCode;
import com.example.authnexus.payload.JwtToken;
import com.example.authnexus.security.JwtTokenProvider;
import com.example.authnexus.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public JwtToken refreshToken(HttpServletRequest request) {

        String accessToken = request.getHeader("Authorization");

        if (accessToken == null) {
            throw new ApiResponseException(ExceptionCode.ERROR_BAD_REQUEST, "PLEASE_PASS_ON_THE_TOKEN_INFORMATION");
        }

        Authentication authentication = jwtTokenProvider.checkRefreshToken(request);

        Member member = memberRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(() -> {
            throw new ApiResponseException(ExceptionCode.ERROR_NOT_FOUND, "NOT_FOUND_MEMBER_INFORMATION");
        });

        JwtToken newToken = jwtTokenProvider.generateToken(member);
        jwtTokenProvider.deleteRefreshToken(accessToken);

        return newToken;
    }

}
