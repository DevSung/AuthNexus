package com.example.authnexus.service.impl;

import com.example.authnexus.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Override
    @Transactional
    public String refreshToken(HttpServletRequest request) {

        return null;
    }
}
