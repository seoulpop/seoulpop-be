package com.ssafy.seoulpop.member.domain.client;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import com.ssafy.seoulpop.member.dto.KakaoLogoutResponse;
import com.ssafy.seoulpop.member.dto.KakaoMemberResponse;
import com.ssafy.seoulpop.member.dto.KakaoToken;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;


public interface KakaoApiClient {

    @PostExchange(url = "https://kauth.kakao.com/oauth/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    KakaoToken fetchToken(@RequestParam MultiValueMap<String, String> params);

    @GetExchange("https://kapi.kakao.com/v2/user/me")
    KakaoMemberResponse fetchMember(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String bearerToken);

    @PostExchange("https://kapi.kakao.com/v1/user/logout")
    KakaoLogoutResponse logout(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String bearerToken);
}
