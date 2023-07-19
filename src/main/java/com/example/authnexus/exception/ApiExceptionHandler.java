package com.example.authnexus.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiResponseException.class)
    public ResponseEntity<ApiExceptionResponse> handleApiResponseException(ApiResponseException ex, HttpServletRequest request) {
        log.error("errorCode:{}, uri:{}, message:{}", ex.getApiExceptionCode(), request.getRequestURI(), ex.getDetailMessage());

        HttpStatus httpStatus;

        if (ex.getApiExceptionCode().equals("400")) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (ex.getApiExceptionCode().equals("404")) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ApiExceptionResponse exceptionResponse = ApiExceptionResponse.builder()
                .status(ex.getApiExceptionCode())
                .mag(ex.getMsg())
                .detail(ex.getDetailMessage())
                .build();

        return ResponseEntity.status(httpStatus).body(exceptionResponse);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        // 기타 예외 처리를 위한 로직을 구현할 수 있음
        // 예: 잘못된 요청, 유효성 검사 실패 등

        // 상태 코드와 메시지를 포함한 응답 객체 생성
        ApiExceptionResponse exceptionResponse = ApiExceptionResponse.builder()
                .status(status.toString())
                .mag(status.getReasonPhrase())
                .detail(ex.getMessage())
                .build();

        return ResponseEntity.status(status).body(exceptionResponse);
    }
}

