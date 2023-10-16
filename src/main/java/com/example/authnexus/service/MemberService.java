package com.example.authnexus.service;

import com.example.authnexus.payload.*;

import java.util.List;
import java.util.Map;

public interface MemberService {

    Map<String, Object> addUser(SignUpRequest signUpRequest);

    List<MemberInfoResponse> getUsers();

    JwtToken login(LoginRequest loginRequest);

    Boolean logout(Long idx);

    MemberInfoResponse getUser(Long idx);

    Boolean updateGender(MemberUpdateRequest request);

}
