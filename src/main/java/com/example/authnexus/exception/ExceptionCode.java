package com.example.authnexus.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    ERROR_NOT_FOUND("404", "ERROR_NOT_FOUND"),
    ERROR_BAD_REQUEST("400", "ERROR_BAD_REQUEST"),
    ERROR_ACCESS_DENIED("403", "ERROR_ACCESS_DENIED");

    private final String code;
    private final String msg;

    ExceptionCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
