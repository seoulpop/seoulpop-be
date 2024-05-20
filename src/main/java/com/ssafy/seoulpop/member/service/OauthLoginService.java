package com.ssafy.seoulpop.member.service;

import static com.ssafy.seoulpop.exception.ErrorCode.INVALID_MEMBER_WITHDRAWN;

import com.ssafy.seoulpop.exception.BaseException;
import com.ssafy.seoulpop.member.domain.Member;
import com.ssafy.seoulpop.member.domain.authcode.AuthCodeRequestUrlProviderComposite;
import com.ssafy.seoulpop.member.domain.client.OauthMemberClientComposite;
import com.ssafy.seoulpop.member.domain.type.OauthServerType;
import com.ssafy.seoulpop.member.dto.LoginDto;
import com.ssafy.seoulpop.member.dto.OauthDto;
import com.ssafy.seoulpop.member.dto.SignUpRequest;
import com.ssafy.seoulpop.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthLoginService {

    private final MemberService memberService;
    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final RedisTemplate<String, Object> tokenRedisTemplate;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public LoginDto login(OauthServerType oauthServerType, String authCode) {
        OauthDto dto = oauthMemberClientComposite.fetch(oauthServerType, authCode);

        Member member = memberRepository.findByOauthId(dto.member().getOauthId()).orElseGet(() -> null);

        // redis oauthAccessToken 저장
        HashOperations<String, Object, Object> hashOperations = tokenRedisTemplate.opsForHash();
        hashOperations.put(dto.member().getOauthId().getOauthServerId(), "oauthAccessToken", dto.accessToken());

        if (member == null) { // 가입하지 않은 유저일 경우
            return memberService.createMember(// 회원가입
                SignUpRequest.builder()
                    .oauthId(dto.member().getOauthId().getOauthServerId())
                    .kakaoNickname(dto.member().getName())
                    .build()
            );
        } else if (member.getDeleted()) { // 탈퇴한 회원일 경우
            throw new BaseException(INVALID_MEMBER_WITHDRAWN);
        }

        return LoginDto.builder()// 로그인
            .oauthId(member.getOauthId().getOauthServerId())
            .kakaoNickname(member.getName())
            .accessToken(tokenService.createToken(member))
            .refreshToken(tokenService.createRefreshToken(member))
            .build();
    }
}
