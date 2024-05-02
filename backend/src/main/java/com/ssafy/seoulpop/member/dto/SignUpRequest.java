package com.ssafy.seoulpop.member.dto;

public record SignUpRequest(
    String oauthId,
    String kakaoNickname
) {

}
