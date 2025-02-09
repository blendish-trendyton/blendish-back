package com.example.blendish.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {
    OK(HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),
    CREATED(HttpStatus.CREATED, "새로운 리소스가 성공적으로 생성되었습니다."),
    ACCEPTED(HttpStatus.ACCEPTED, "요청이 성공적으로 처리되었습니다. 처리가 지연될 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    SuccessCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
