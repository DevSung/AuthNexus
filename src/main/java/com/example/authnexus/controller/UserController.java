package com.example.authnexus.controller;

import com.example.authnexus.payload.SignUpRequest;
import com.example.authnexus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public String sample() {
        return "Home";
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> addUser(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(userService.addUser(signUpRequest));
    }


}
