package com.example.authnexus.payload;

import com.example.authnexus.domain.entity.Member;
import com.example.authnexus.domain.entity.MemberRole;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    private String userId;

    private String password;

    private String email;

    private String name;

    private String gender;

    private LocalDate birthday;

    private String addr;

    private String tel;

    private String[] roles;

    public Member toEntity(String encoderPassword) {
        return Member.builder()
                .userId(this.userId)
                .password(encoderPassword)
                .email(this.email)
                .name(this.name)
                .gender(this.gender)
                .birthday(this.birthday)
                .addr(this.addr)
                .tel(this.tel)
                .build();
    }

    public MemberRole toRoleEntity(Member member, String role) {
        return MemberRole.builder()
                .member(member)
                .role(role)
                .build();
    }
}
