package com.example.authnexus.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiExceptionResponse {

    private String status;
    private String mag;
    private String detail;

}
