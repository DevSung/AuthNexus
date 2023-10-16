package com.example.authnexus.domain.repository;

import com.example.authnexus.domain.entity.Member;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @NotNull
    @EntityGraph(value = "roles")
    List<Member> findAll();

    Optional<Member> findByUserId(String userId);

    boolean existsByUserId(String userId);

}
