package com.example.authnexus.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
public class LoginRequest {

    @Schema(description = "아이디")
    private String userId;

    @Schema(description = "패스워드")
    private String password;

}
