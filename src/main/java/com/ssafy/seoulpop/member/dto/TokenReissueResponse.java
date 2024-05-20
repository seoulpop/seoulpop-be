package com.ssafy.seoulpop.member.dto;

import lombok.Builder;

@Builder
public record TokenReissueResponse(
    String accessToken
) {

}
