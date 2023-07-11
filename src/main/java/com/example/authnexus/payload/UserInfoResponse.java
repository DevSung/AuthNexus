package com.example.authnexus.payload;

import com.example.authnexus.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private Long idx;

    private String userId;

    private String password;

    private String email;

    private String gender;

    private LocalDate birthday;

    private String addr;

    private String tel;

    public UserInfoResponse(User user) {
        this.idx = user.getIdx();
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.gender = user.getGender();
        this.birthday = user.getBirthday();
        this.addr = user.getAddr();
        this.tel = user.getTel();
    }

}
