package com.example.authnexus.domain.common.repository;

import com.example.authnexus.domain.common.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByMemberIdxAndToken(Long idx, String token);

    boolean existsByMemberIdx(Long idx);

    void deleteByMemberIdx(Long idx);
}
