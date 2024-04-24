package com.ssafy.seoulpop.member.domain.type;

import static java.util.Locale.*;

public enum OauthServerType {
	KAKAO,
	;

	public static OauthServerType fromName(String type) {
		return OauthServerType.valueOf(type.toUpperCase(ENGLISH));
	}
}
