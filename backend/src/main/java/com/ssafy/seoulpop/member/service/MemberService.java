package com.ssafy.seoulpop.member.service;

import static com.ssafy.seoulpop.member.domain.type.OauthServerType.KAKAO;

import com.ssafy.seoulpop.member.domain.Member;
import com.ssafy.seoulpop.member.domain.OauthId;
import com.ssafy.seoulpop.member.domain.type.OauthServerType;
import com.ssafy.seoulpop.member.dto.LoginDto;
import com.ssafy.seoulpop.member.dto.SignUpRequest;
import com.ssafy.seoulpop.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final OauthService oauthService;
    private final TokenService tokenService;

    public LoginDto createMember(SignUpRequest request) {
        Member member = memberRepository.save(
            Member.builder()
                .oauthId(new OauthId(request.oauthId(), KAKAO))
                .name(request.kakaoNickname())
                .build()
        );

        return LoginDto.builder()
            .kakaoNickname(member.getName())
            .accessToken(tokenService.createToken(member))
            .refreshToken(tokenService.createRefreshToken(member))
            .build();
    }

    public void deleteMember(HttpServletResponse response, OauthServerType oauthServerType, Member member) {
        oauthService.logout(response, oauthServerType, member.getOauthId().getOauthServerId()); // 카카오 로그아웃
        tokenService.deleteHeader(response); // header에서 accesstoken, refreshtoken 삭제
        member.setDeleted(true);
        memberRepository.save(member);
    }
}
