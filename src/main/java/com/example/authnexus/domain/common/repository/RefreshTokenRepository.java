package com.example.authnexus.domain.common.repository;

import com.example.authnexus.domain.common.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByUserIdAndToken(String userId, String token);
    void deleteByUserIdAndToken(String userId, String token);
}
