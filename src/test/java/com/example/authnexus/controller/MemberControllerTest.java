package com.example.authnexus.controller;

import com.example.authnexus.payload.LoginRequest;
import com.example.authnexus.payload.MemberInfoResponse;
import com.example.authnexus.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("local")
class MemberControllerTest {

    @Autowired
    MemberService memberService;

    @Test
    void addUser() {
    }

    @Test
    void login() {
    }

    @Test
    void getUsers() {
    }

    @Test
    void getUser() {
    }

}