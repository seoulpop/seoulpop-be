package com.ssafy.seoulpop.member.dto;

import lombok.Builder;

@Builder
public record LoginDto(
    String oauthId,
    String kakaoNickname,
    String accessToken,
    String refreshToken
) {

}
