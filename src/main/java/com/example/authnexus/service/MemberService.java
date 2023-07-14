package com.example.authnexus.service;

import com.example.authnexus.payload.JwtToken;
import com.example.authnexus.payload.LoginRequest;
import com.example.authnexus.payload.MemberInfoResponse;
import com.example.authnexus.payload.SignUpRequest;

import java.util.List;
import java.util.Map;

public interface MemberService {

    Map<String, Object> addUser(SignUpRequest signUpRequest);

    List<MemberInfoResponse> getUsers();

    JwtToken login(LoginRequest loginRequest);
}
