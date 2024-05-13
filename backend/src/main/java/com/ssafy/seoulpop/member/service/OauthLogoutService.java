package com.ssafy.seoulpop.member.service;

import com.ssafy.seoulpop.member.domain.client.OauthMemberClientComposite;
import com.ssafy.seoulpop.member.domain.type.OauthServerType;
import com.ssafy.seoulpop.member.dto.KakaoLogoutResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthLogoutService {
    private final TokenService tokenService;
    private final OauthMemberClientComposite oauthMemberClientComposite;

    public KakaoLogoutResponse logout(HttpServletResponse response, OauthServerType oauthServerType, String oauthId) {
        tokenService.deleteHeader(response);
        return oauthMemberClientComposite.logout(oauthServerType, oauthId);
    }
}
