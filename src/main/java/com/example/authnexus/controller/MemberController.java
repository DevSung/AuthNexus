package com.example.authnexus.controller;

import com.example.authnexus.payload.LoginRequest;
import com.example.authnexus.payload.SignUpRequest;
import com.example.authnexus.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String sample() {
        return "Home";
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> addUser(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(memberService.addUser(signUpRequest));
    }

    @GetMapping("")
    public ResponseEntity<Object> getUsers() {
        return ResponseEntity.ok(memberService.getUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(memberService.login(loginRequest));
    }

}
