package com.example.authnexus.controller;

import com.example.authnexus.payload.LoginRequest;
import com.example.authnexus.payload.SignUpRequest;
import com.example.authnexus.payload.common.ResponseData;
import com.example.authnexus.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 서비스")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입")
    @PostMapping("")
    public ResponseEntity<Object> addUser(@RequestBody SignUpRequest signUpRequest) {
        return ResponseData.success(memberService.addUser(signUpRequest));
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        return ResponseData.success(memberService.login(loginRequest));
    }

    @Operation(summary = "회원목록")
    @GetMapping("")
    public ResponseEntity<Object> getUsers() {
        return ResponseData.success(memberService.getUsers());
    }

}
