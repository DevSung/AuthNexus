package com.example.authnexus.service;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    String refreshToken(HttpServletRequest request);
}
