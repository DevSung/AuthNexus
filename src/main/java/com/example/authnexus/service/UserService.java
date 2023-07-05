package com.example.authnexus.service;

import com.example.authnexus.payload.SignUpRequest;

import java.util.Map;

public interface UserService {

    Map<String, Object> addUser(SignUpRequest signUpRequest);

}
