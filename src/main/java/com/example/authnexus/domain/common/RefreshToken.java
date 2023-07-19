package com.example.authnexus.domain.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token", schema = "user")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long idx;

    @Column(name = "user_Id", nullable = false)
    private String userId;

    @Column(name = "token", nullable = false)
    private String token;

}
