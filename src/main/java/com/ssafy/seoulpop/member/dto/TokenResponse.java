package com.ssafy.seoulpop.member.dto;

import lombok.Builder;

@Builder
public record TokenResponse(
    String accessToken,
    String refreshToken
) {

}
