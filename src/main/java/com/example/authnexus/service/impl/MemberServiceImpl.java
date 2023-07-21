package com.example.authnexus.service.impl;


import com.example.authnexus.domain.member.Member;
import com.example.authnexus.domain.member.MemberRole;
import com.example.authnexus.domain.member.repository.MemberRepository;
import com.example.authnexus.domain.member.repository.MemberRoleRepository;
import com.example.authnexus.exception.ApiResponseException;
import com.example.authnexus.exception.ExceptionCode;
import com.example.authnexus.payload.JwtToken;
import com.example.authnexus.payload.LoginRequest;
import com.example.authnexus.payload.MemberInfoResponse;
import com.example.authnexus.payload.SignUpRequest;
import com.example.authnexus.security.JwtTokenProvider;
import com.example.authnexus.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final Argon2PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Map<String, Object> addUser(SignUpRequest signUpRequest) {

        if (memberRepository.existsByUserId(signUpRequest.getUserId())) {
            throw new ValidationException("동일한 아이디가 존재합니다.");
        }

        // Argon2 알고리즘으로 패스워드 암호화
        String encoderPassword = passwordEncoder.encode(signUpRequest.getPassword());

        // 회원 저장
        Member member = Member.builder()
                .userId(signUpRequest.getUserId())
                .password(encoderPassword)
                .email(signUpRequest.getEmail())
                .name(signUpRequest.getName())
                .gender(signUpRequest.getGender())
                .birthday(signUpRequest.getBirthday())
                .addr(signUpRequest.getAddr())
                .tel(signUpRequest.getTel())
                .build();

        memberRepository.save(member);
        memberRepository.flush();

        List<MemberRole> roles = new ArrayList<>();

        // 회원 권한 저장
        if (!signUpRequest.getRolesList().isEmpty()) {
            signUpRequest.getRolesList().forEach(r -> {
                MemberRole role = MemberRole.builder()
                        .member(member)
                        .role(r.getRole())
                        .build();
                roles.add(role);
            });
        }
        memberRoleRepository.saveAll(roles);

        Map<String, Object> map = new HashMap<>();
        map.put("id", member.getUserId());
        map.put("pw", member.getPassword());
        map.put("role", roles.stream().map(MemberRole::getRole).collect(Collectors.joining(",")));

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

        Member member = memberRepository.findByUserId(loginRequest.getUserId()).orElseThrow(() -> {
            throw new ApiResponseException(ExceptionCode.ERROR_NOT_FOUND, "아이디가 일치하지 않습니다.");
        });

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new ApiResponseException(ExceptionCode.ERROR_NOT_FOUND, "비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.generateToken(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberInfoResponse getUser(Long idx) {
        Member member = memberRepository.findById(idx).orElseThrow(() -> {
            throw new ApiResponseException(ExceptionCode.ERROR_NOT_FOUND, "회원 정보가 없습니다.");
        });
        return new MemberInfoResponse(member);
    }


}
