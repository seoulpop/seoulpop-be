package com.ssafy.seoulpop.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // member
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다."),
    INVALID_MEMBER_WITHDRAWN(HttpStatus.FORBIDDEN, "당신이 요청한 사용자는 탈퇴했으므로 접근할 수 없습니다."),

    //4xx
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "유효성 검사를 통과하지 못했습니다."),
    FCM_TOKEN_NOT_REGISTERED_ERROR(HttpStatus.BAD_REQUEST, "FCM 토큰이 등록되어있지 않습니다."),
    FCM_TOKEN_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "FCM 토큰을 찾을 수 없습니다."),
    INVALID_LEVEL_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 레벨 값입니다."),
    INVALID_LOCATION_ERROR(HttpStatus.BAD_REQUEST, "유효하지 않은 위,경도입니다."),
    MEMBER_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    HISTORY_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "해당 역사 정보를 찾을 수 없습니다."),
    NOTIFICATION_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "해당 푸시 알림을 찾을 수 없습니다."),

    //5xx
    Internal_Server_Error(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    private final HttpStatus httpStatus;
    private final String errorMsg;
}
