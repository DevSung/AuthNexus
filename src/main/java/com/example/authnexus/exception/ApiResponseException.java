package com.example.authnexus.exception;

import lombok.Getter;

@Getter
public class ApiResponseException extends RuntimeException {

    private final String apiExceptionCode;
    private final String detailMessage;
    private final String msg;

    public ApiResponseException(ExceptionCode code, String detailMessage) {
        this.apiExceptionCode = code.getCode();
        this.msg = code.getMsg();
        this.detailMessage = detailMessage;
    }

}
