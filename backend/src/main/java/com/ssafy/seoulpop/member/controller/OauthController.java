package com.ssafy.seoulpop.member.controller;

import com.ssafy.seoulpop.member.domain.Member;
import com.ssafy.seoulpop.member.domain.type.OauthServerType;
import com.ssafy.seoulpop.member.dto.KakaoLogoutResponse;
import com.ssafy.seoulpop.member.dto.LoginDto;
import com.ssafy.seoulpop.member.dto.LoginResponse;
import com.ssafy.seoulpop.member.service.OauthLoginService;
import com.ssafy.seoulpop.member.service.OauthLogoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 대행 컨트롤러", description = "Oauth 관련 로그인 및 로그아웃 기능 등이 포함되어 있음")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/oauth")
public class OauthController {

    private final OauthLoginService oauthLoginService;
    private final OauthLogoutService oauthLogoutService;

    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    public ResponseEntity<Void> redirectAuthCodeRequestUrl(
        @PathVariable OauthServerType oauthServerType,
        HttpServletResponse response
    ) {
        String redirectUrl = oauthLoginService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping("/redirected/kakao")
    public ResponseEntity<Void> redirectAuthCodeRequestUrl(
        @RequestParam String code
    ) {
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "카카오 로그인",
        description = "카카오 계정을 통해 로그인을 진행한다.")
    @GetMapping("/login/{oauthServerType}")
    public ResponseEntity<LoginResponse> login(
        HttpServletResponse response,
        @PathVariable OauthServerType oauthServerType,
        @RequestParam String code
    ) {

        LoginDto login = oauthLoginService.login(oauthServerType, code);

        Cookie cookie = new Cookie("refreshToken", login.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        // 기존의 쿠키 설정을 문자열로 변환
        String cookieValue = "refreshToken=" + login.refreshToken()
            + "; HttpOnly; Secure; Path=/; SameSite=None";

        // 응답 헤더에 쿠키 추가
        response.addHeader("Set-Cookie", cookieValue);

        return ResponseEntity.ok(LoginResponse
            .builder()
            .oauthId(login.oauthId())
            .kakaoNickname(login.kakaoNickname())
            .accessToken(login.accessToken())
            .build());
    }

    @Operation(
        summary = "로그아웃",
        description = "로그아웃 이후 헤더와 쿠키를 초기화시킨다.")
    @PostMapping("/logout/{oauthServerType}")
    public ResponseEntity<KakaoLogoutResponse> logout(
        @AuthenticationPrincipal Member member,
        HttpServletResponse response,
        @PathVariable OauthServerType oauthServerType
    ) {
        return ResponseEntity.ok(
            oauthLogoutService.logout(response, oauthServerType, member.getOauthId().getOauthServerId()));
    }
}
