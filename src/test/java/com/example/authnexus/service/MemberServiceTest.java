package com.example.authnexus.service;

import com.example.authnexus.domain.member.Member;
import com.example.authnexus.domain.member.repository.MemberRepository;
import com.example.authnexus.domain.member.repository.MemberRoleRepository;
import com.example.authnexus.payload.JwtToken;
import com.example.authnexus.payload.LoginRequest;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
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
    void T() {

        String t = "600";
        String t2 = "659";

        // 3자리 숫자를 4자리로 맞추고 앞에 0을 추가
        t = String.format("%04d", Integer.parseInt(t));
        t2 = String.format("%04d", Integer.parseInt(t2));

        // 문자열을 LocalTime으로 파싱하기 위한 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");

        // 문자열을 LocalTime으로 변환
        LocalTime localTime1 = LocalTime.parse(t, formatter);
        LocalTime localTime2 = LocalTime.parse(t2, formatter);

        System.out.println(localTime1); // 출력: 05:00
        System.out.println(localTime2); // 출력: 05:59

    }

    @Test
    void TT() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH0000");

        String formattedDateTime = now.format(formatter);
        System.out.println(formattedDateTime);
    }

    @Test
    void Test() {
        int[][] array = new int[5][2];
        array[0][0] = 10;
        array[0][1] = 7;
        array[1][0] = 12;
        array[1][1] = 3;
        array[2][0] = 8;
        array[2][1] = 15;
        array[3][0] = 14;
        array[3][1] = 7;
        array[4][0] = 5;
        array[4][1] = 15;

        System.out.println(solution(array));
    }

    @Test
    @DisplayName("Test")
    public int solution(int[][] sizes) {
        int maxW = 0; // 가로 max
        int maxH = 0; // 세로 max

        for (int[] size : sizes) {
            int w = size[0];
            int h = size[1];

            if (w >= h) {
                if (w >= maxW) {
                    maxW = w;
                }
                if (h > maxH) {
                    maxH = h;
                }
            } else {
                if (h >= maxW) {
                    maxW = h;
                }
                if (w > maxH) {
                    maxH = w;
                }
            }
        }

        return maxW * maxH;
    }

    @Test
    void pay() {
        // 첫 결제일
        LocalDate paymentDate = LocalDate.of(2023, 1, 21);

        // 결제 간격 (1달)
        int payInterval = 1;

        // 예상 결제일 목록을 저장할 리스트
        List<LocalDate> paymentDates = new ArrayList<>();

        // 올해 결제 예상일 계산
        while (paymentDate.isBefore(LocalDate.now()) || paymentDate.isEqual(LocalDate.now())) {
            paymentDate = paymentDate.plusMonths(1);
            paymentDates.add(paymentDate);
        }

        // 결제일 목록 출력
        System.out.println("올해 결제 예상 일자 목록:");
        for (LocalDate p : paymentDates) {
            System.out.println(p);
        }
    }
}


