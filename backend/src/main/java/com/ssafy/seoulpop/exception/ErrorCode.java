package com.ssafy.seoulpop.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //4xx
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "유효성 검사를 통과하지 못했습니다."),
    FCM_TOKEN_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "FCM 토큰을 찾을 수 없습니다."),
    INVALID_LOCATION_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 레벨 값입니다."),
    INVALID_LEVEL_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 위,경도입니다."),
    MEMBER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    HISTORY_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "해당 역사 정보를 찾을 수 없습니다."),

    //5xx
    Internal_Server_Error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    private final HttpStatus httpStatus;
    private final String errorMsg;
}
