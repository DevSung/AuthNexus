package com.example.authnexus.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SignUpRequest {

    private String userId;

    private String password;

    private String email;

    private String name;

    private String gender;

    private LocalDate birthday;

    private String addr;

    private String tel;

    private List<Roles> rolesList;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Roles {
        private String role;
    }

}
