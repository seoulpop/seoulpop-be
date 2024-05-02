package com.ssafy.seoulpop.member.domain.client;

import com.ssafy.seoulpop.config.KakaoOauthConfig;
import com.ssafy.seoulpop.member.domain.Member;
import com.ssafy.seoulpop.member.domain.type.OauthServerType;
import com.ssafy.seoulpop.member.dto.KakaoLogoutResponse;
import com.ssafy.seoulpop.member.dto.KakaoMemberResponse;
import com.ssafy.seoulpop.member.dto.KakaoToken;
import com.ssafy.seoulpop.member.dto.OauthDto;
import com.ssafy.seoulpop.member.util.MemberMapper;
import com.ssafy.seoulpop.member.util.OauthIdMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoMemberClient implements OauthMemberClient {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoOauthConfig kakaoOauthConfig;
    private final RedisTemplate<String, Object> tokenRedisTemplate;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    @Override
    public OauthDto fetch(String authCode) {
        KakaoToken tokenInfo = kakaoApiClient.fetchToken(tokenRequestParams(authCode));
        KakaoMemberResponse kakaoMemberResponse = kakaoApiClient.fetchMember("Bearer " + tokenInfo.accessToken());
        Member member = MemberMapper.toMember(kakaoMemberResponse);
        return OauthIdMapper.fromMember(member, tokenInfo.accessToken());
    }

    @Override
    public KakaoLogoutResponse logout(String oauthId) {
        String oauthAccessToken = (String) tokenRedisTemplate.opsForHash().get(oauthId, "oauthAccessToken");
        return kakaoApiClient.logout("Bearer " + oauthAccessToken);
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOauthConfig.clientId());
        params.add("redirect_uri", kakaoOauthConfig.redirectUri());
        params.add("code", authCode);
        return params;
    }
}
