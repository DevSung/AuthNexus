package com.example.authnexus.controller;

import com.example.authnexus.payload.JwtToken;
import com.example.authnexus.payload.LoginRequest;
import com.example.authnexus.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "JWT Token 발행")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final MemberService memberService;

    @Operation(summary = "토큰 발행")
    @PostMapping("/token")
    public ResponseEntity<Object> getToken(@RequestBody LoginRequest loginRequest) {

        JwtToken jwtToken = memberService.login(loginRequest);
        String token = "Bearer " + jwtToken.getAccessToken();

        return ResponseEntity.ok(token);
    }

}
