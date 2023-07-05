package com.example.authnexus.payload;

import com.example.authnexus.domain.User;
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

    private String gender;

    private LocalDate birthday;

    private String address;

    private String tel;

    public User signUp(String encoderPassword) {
        return User.builder()
                .userId(this.userId)
                .password(encoderPassword)
                .email(this.email)
                .gender(this.gender)
                .birthday(this.birthday)
                .address(this.address)
                .tel(this.tel)
                .build();
    }

}
