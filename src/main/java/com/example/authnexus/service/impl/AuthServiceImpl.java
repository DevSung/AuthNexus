package com.example.authnexus.service.impl;

import com.example.authnexus.domain.member.Member;
import com.example.authnexus.domain.member.repository.MemberRepository;
import com.example.authnexus.payload.JwtToken;
import com.example.authnexus.security.JwtTokenProvider;
import com.example.authnexus.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public JwtToken refreshToken(HttpServletRequest request) {
        Authentication authentication = jwtTokenProvider.checkRefreshToken(request);
        Optional<Member> member = memberRepository.findById(Long.valueOf(authentication.getName()));

        return member.map(jwtTokenProvider::generateToken).orElse(null);
    }

}
