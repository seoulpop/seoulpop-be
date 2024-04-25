package com.ssafy.seoulpop.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final int httpStatus;
    private final String errorMsg;
}
