package com.ssafy.seoulpop.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //4xx
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "유효성 검사를 통과하지 못했습니다."),

    //5xx
    Internal_Server_Error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    private final HttpStatus httpStatus;
    private final String errorMsg;
}
