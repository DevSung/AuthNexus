package com.example.authnexus.service;

import com.example.authnexus.payload.JwtToken;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    JwtToken refreshToken(HttpServletRequest request);
}
