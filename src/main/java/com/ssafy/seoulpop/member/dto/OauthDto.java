package com.ssafy.seoulpop.member.dto;

import com.ssafy.seoulpop.member.domain.Member;
import lombok.Builder;

@Builder
public record OauthDto(
    Member member,
    String accessToken
) {

}
