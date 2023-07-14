package com.example.authnexus.service.impl;


import com.example.authnexus.domain.Member;
import com.example.authnexus.payload.JwtToken;
import com.example.authnexus.payload.LoginRequest;
import com.example.authnexus.payload.MemberInfoResponse;
import com.example.authnexus.payload.SignUpRequest;
import com.example.authnexus.repository.MemberRepository;
import com.example.authnexus.security.JwtTokenProvider;
import com.example.authnexus.security.SecurityConfig;
import com.example.authnexus.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityConfig securityConfig;

    @Override
    @Transactional
    public Map<String, Object> addUser(SignUpRequest signUpRequest) {

        String encoderPassword = securityConfig.passwordEncoder().encode(signUpRequest.getPassword());
        Member member = signUpRequest.signUp(encoderPassword);

        memberRepository.save(member);

        Map<String, Object> map = new HashMap<>();
        map.put("id", member.getUserId());
        map.put("pw", member.getPassword());

        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberInfoResponse> getUsers() {
        List<Member> users = memberRepository.findAll();

        if (users.isEmpty()) {
            return new ArrayList<>();
        }

        List<MemberInfoResponse> result = new ArrayList<>();
        users.forEach(u -> result.add(new MemberInfoResponse(u)));

        return result;
    }

    /**
     * login 후 token return
     */
    @Override
    @Transactional
    public JwtToken login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword());

        Member member = memberRepository.findByUserId(loginRequest.getUserId()).orElseThrow(() -> {
            throw new UsernameNotFoundException("userId not found");
        });

        if (!securityConfig.match(loginRequest.getPassword(), member.getPassword())) {
            throw new UsernameNotFoundException("비밀번호가 일치하지 않습니다.");
        }

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }

}
