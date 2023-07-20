package com.example.authnexus.security;

import com.example.authnexus.config.TokenUserResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final TokenUserResolver tokenUserResolver;

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
                        "/api/member/login",
                        "/api/member/join",
                        "/api/auth/token",
                        "/swagger-ui/**").permitAll()
                // 권한("USER")이 필요한 서비스, hasRole은 자동으로 권한 앞에 "ROLE_" Prefix 해줌
                // hasAuthority() -> db값 그대로 equal 비교
                .antMatchers("/api/**").hasAuthority("USER")

                // 세션을 사용하지 않겠음
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // JwtFilter 사용하겠다고 선언
                .and()
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public Argon2PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

}
