package com.ssafy.seoulpop.member.domain.client;

import com.ssafy.seoulpop.member.domain.type.OauthServerType;
import com.ssafy.seoulpop.member.dto.KakaoLogoutResponse;
import com.ssafy.seoulpop.member.dto.OauthDto;

public interface OauthMemberClient {

    OauthServerType supportServer();

    OauthDto fetch(String code);

    KakaoLogoutResponse logout(String oauthId);
}
