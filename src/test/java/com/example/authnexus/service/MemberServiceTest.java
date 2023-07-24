package com.example.authnexus.service;

import com.example.authnexus.domain.member.Member;
import com.example.authnexus.domain.member.repository.MemberRepository;
import com.example.authnexus.domain.member.repository.MemberRoleRepository;
import com.example.authnexus.payload.JwtToken;
import com.example.authnexus.payload.LoginRequest;
import com.example.authnexus.payload.MemberInfoResponse;
import com.example.authnexus.payload.SignUpRequest;
import com.example.authnexus.security.JwtTokenProvider;
import com.example.authnexus.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    /**
     * @Mock 모의 객체를 생성해준다.
     * @InjectMocks 테스트 대상 객체에 모의 객체를 주입해준다.
     */

    @Mock
    MemberRepository memberRepository;

    @Mock
    MemberRoleRepository memberRoleRepository;

    @Mock
    Argon2PasswordEncoder passwordEncoder;

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    MemberServiceImpl memberService;

    @Test
    @DisplayName("회원가입 테스트")
    void addUser() {
        // given
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUserId("Testeeee");
        signUpRequest.setPassword("1234");
        signUpRequest.setRoles(new String[]{"USER"});

        // when
        Map<String, Object> result = memberService.addUser(signUpRequest);

        // then
        assertThat("USER").isEqualTo(result.get("role"));
    }

    @Test
    @DisplayName("토큰 발급 테스트")
    void loginTest() {
        // given
        String pw = passwordEncoder.encode("1234");
        Member member = Member.builder()
                .idx(1L)
                .userId("test")
                .password(pw)
                .build();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId(member.getUserId());
        loginRequest.setPassword("1234");

        Optional<Member> memberOptional = Optional.of(member);

        // stub
        when(memberRepository.findByUserId(any())).thenReturn(memberOptional);
        when(passwordEncoder.matches("1234", pw)).thenReturn(true);

        JwtToken jwtToken = JwtToken.builder()
                .accessToken("ddd")
                .refreshToken("dddd")
                .build();

        when(jwtTokenProvider.generateToken(memberOptional.get())).thenReturn(jwtToken);

        // when
        JwtToken result = memberService.login(loginRequest);

        // then
        System.out.println(result.getAccessToken());
    }

    @Test
    @DisplayName("회원목록")
    void getUser() {
        List<MemberInfoResponse> infos = memberService.getUsers();
    }

}