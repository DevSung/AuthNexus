package com.example.authnexus.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // HttpBasic 비활성화
                .httpBasic().disable()
                // csrf 비활성화 (이유는 아래 참고)
                .csrf().disable()
                .cors().and()
                // 인증 없이도 접근이 가능
                .authorizeRequests()
                .antMatchers(
                        "/api/member/", // test api
                        "/api/member/login",
                        "/api/member/sign-up",
                        "/api/auth/token",
                        "/swagger-ui/**").permitAll()
                // 권한("USER")이 필요한 서비스
                .antMatchers("/api/**").hasAuthority("USER")

                // 세션을 사용하지 않겠음
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // JwtFilter 사용하겠다고 선언
                .and()
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public boolean match(String password, String memberPassword) {
        BCryptPasswordEncoder encoder = passwordEncoder();
        return encoder.matches(password, memberPassword);
    }


}
