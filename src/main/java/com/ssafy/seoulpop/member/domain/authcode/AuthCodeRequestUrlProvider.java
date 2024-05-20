package com.ssafy.seoulpop.member.domain.authcode;

import com.ssafy.seoulpop.member.domain.type.OauthServerType;

public interface AuthCodeRequestUrlProvider {

    OauthServerType supportServer();

    String provide();
}
