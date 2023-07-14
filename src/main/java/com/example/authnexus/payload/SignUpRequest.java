package com.example.authnexus.payload;

import com.example.authnexus.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
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

    public Member signUp(String encoderPassword) {
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

}
