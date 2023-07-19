package com.example.authnexus.controller;

import com.example.authnexus.payload.JwtToken;
import com.example.authnexus.payload.LoginRequest;
import com.example.authnexus.payload.common.ResponseData;
import com.example.authnexus.service.AuthService;
import com.example.authnexus.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "JWT Token 발행")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;
    private final AuthService authService;

    @Operation(summary = "토큰 발행 (Swagger 발급용)")
    @PostMapping("/token")
    public ResponseEntity<Object> getToken(@RequestBody LoginRequest loginRequest) {

        JwtToken jwtToken = memberService.login(loginRequest);
        String token = "Bearer " + jwtToken.getAccessToken();

        return ResponseEntity.ok(token);
    }

    @GetMapping("/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        return ResponseData.success(authService.refreshToken(request));
    }

}
