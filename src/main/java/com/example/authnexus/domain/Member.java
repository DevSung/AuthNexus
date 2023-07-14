package com.example.authnexus.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member", schema = "user")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long idx;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "addr")
    private String addr;

    @Column(name = "tel")
    private String tel;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "member")
    private List<MemberRole> roles;
}
