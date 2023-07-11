package com.example.authnexus.service;

import com.example.authnexus.payload.SignUpRequest;
import com.example.authnexus.payload.UserInfoResponse;

import java.util.List;
import java.util.Map;

public interface UserService {

    Map<String, Object> addUser(SignUpRequest signUpRequest);

    List<UserInfoResponse> getUsers();
}
