package com.example.authnexus.config;

import com.example.authnexus.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class TokenUserResolverConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public TokenUserResolverConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new TokenUserResolver(jwtTokenProvider));
    }

}
