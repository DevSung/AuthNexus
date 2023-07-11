package com.example.authnexus;

import com.example.authnexus.domain.User;
import com.example.authnexus.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AuthNexusApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Test
    void firstTest() {
        Long idx = 1L;
        Optional<User> user = userRepository.findById(idx);

        assertTrue(user.isPresent());
    }

}
