package com.ssafy.seoulpop.member.dto;

import lombok.Builder;

@Builder
public record SignUpRequest(
    String oauthId,
    String kakaoNickname
) {

}
