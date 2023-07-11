package com.example.authnexus.service.impl;

import com.example.authnexus.config.SecurityConfig;
import com.example.authnexus.domain.User;
import com.example.authnexus.payload.SignUpRequest;
import com.example.authnexus.payload.UserInfoResponse;
import com.example.authnexus.repository.UserRepository;
import com.example.authnexus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;

    @Override
    @Transactional
    public Map<String, Object> addUser(SignUpRequest signUpRequest) {

        String encoderPassword = securityConfig.passwordEncoder().encode(signUpRequest.getPassword());
        User user = signUpRequest.signUp(encoderPassword);

        userRepository.save(user);

        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getUserId());
        map.put("pw", user.getPassword());

        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInfoResponse> getUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            return new ArrayList<>();
        }

        List<UserInfoResponse> result = new ArrayList<>();
        users.forEach(u -> result.add(new UserInfoResponse(u)));

        return result;
    }

}
