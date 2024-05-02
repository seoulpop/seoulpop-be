package com.ssafy.seoulpop.member.controller;

import com.ssafy.seoulpop.member.domain.Member;
import com.ssafy.seoulpop.member.domain.type.OauthServerType;
import com.ssafy.seoulpop.member.dto.LoginDto;
import com.ssafy.seoulpop.member.dto.SignUpRequest;
import com.ssafy.seoulpop.member.dto.SignUpResponse;
import com.ssafy.seoulpop.member.dto.TokenReissueResponse;
import com.ssafy.seoulpop.member.dto.TokenResponse;
import com.ssafy.seoulpop.member.service.MemberService;
import com.ssafy.seoulpop.member.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 컨트롤러", description = "회원의 인증 및 인가 기능, 회원가입, 닉네임 수정, 조회 등의 기능이 포함되어 있음")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;

    @Operation(
        summary = "access 토큰 재발급",
        description = "만료된 access token을 재발급 한다.")
    @PostMapping("/reissue")
    public ResponseEntity<TokenReissueResponse> reissueAccessToken(
        HttpServletRequest request, HttpServletResponse response) {
        TokenResponse tokenResponse = tokenService.reissueAccessToken(request, response);

        return ResponseEntity.ok(
            TokenReissueResponse.builder()
                .accessToken(tokenResponse.accessToken())
                .build()
        );
    }

    @Operation(
        summary = "회원가입",
        description = "카카오 인증 후 입력받은 회원 정보를 바탕으로 회원 가입을 진행한다. 이후 로그인 상태로 전환한다.")
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> addMember(
        HttpServletResponse response,
        @RequestBody SignUpRequest request
    ) {
        LoginDto login = memberService.createMember(request);

        Cookie cookie = new Cookie("refreshToken", login.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        // 기존의 쿠키 설정을 문자열로 변환
        String cookieValue = "refreshToken=" + login.refreshToken()
            + "; HttpOnly; Secure; Path=/; SameSite=None";

        // 응답 헤더에 쿠키 추가
        response.addHeader("Set-Cookie", cookieValue);

        return ResponseEntity.ok(
            SignUpResponse.builder()
                .kakaoNickname(login.kakaoNickname())
                .accessToken(login.accessToken())
                .build()
        );
    }

    @Operation(
        summary = "회원탈퇴",
        description = "회원을 비활성화시킨다. (soft deletion)")
    @DeleteMapping("/withdraw/{oauthServerType}")
    public ResponseEntity<Void> removeMember(
        @AuthenticationPrincipal Member member,
        HttpServletResponse response,
        @PathVariable OauthServerType oauthServerType
    ) {
        memberService.deleteMember(response, oauthServerType, member);
        return ResponseEntity.ok().build();
    }
}