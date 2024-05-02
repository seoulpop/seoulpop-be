package com.ssafy.seoulpop.member.util;

import com.ssafy.seoulpop.member.domain.Member;
import com.ssafy.seoulpop.member.dto.OauthDto;

public class OauthIdMapper {

    public static OauthDto fromMember(Member member, String at) {
        return OauthDto.builder()
            .member(member)
            .accessToken(at)
            .build();
    }
}
