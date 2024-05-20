package com.ssafy.seoulpop.member.dto;

import lombok.Builder;

@Builder
public record MemberResponse(
    Long memberId
) {

}
