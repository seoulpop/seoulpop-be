package com.ssafy.seoulpop.member.dto;

import lombok.Builder;

@Builder
public record LoginResponse(
    String oauthId,
    String kakaoNickname,
    String accessToken
) {

}
