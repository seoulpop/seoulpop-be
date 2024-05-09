package com.ssafy.seoulpop.member.util;

import static com.ssafy.seoulpop.member.domain.type.OauthServerType.KAKAO;

import com.ssafy.seoulpop.member.domain.Member;
import com.ssafy.seoulpop.member.domain.OauthId;
import com.ssafy.seoulpop.member.dto.KakaoMemberResponse;

public class MemberMapper {

    public static Member toMember(KakaoMemberResponse kakaoMemberResponse) {
        return Member.builder()
            .oauthId(new OauthId(String.valueOf(kakaoMemberResponse.id()), KAKAO))
            .name(kakaoMemberResponse.kakaoAccount().profile().nickname())
            .build();
    }
}