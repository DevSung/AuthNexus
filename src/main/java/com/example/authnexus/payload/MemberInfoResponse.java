package com.example.authnexus.payload;

import com.example.authnexus.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponse {

    private Long idx;

    private String userId;

    private String password;

    private String email;

    private String gender;

    private LocalDate birthday;

    private String addr;

    private String tel;

    public MemberInfoResponse(Member member) {
        this.idx = member.getIdx();
        this.userId = member.getUserId();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.gender = member.getGender();
        this.birthday = member.getBirthday();
        this.addr = member.getAddr();
        this.tel = member.getTel();
    }

}
