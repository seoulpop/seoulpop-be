package com.ssafy.seoulpop.notification.controller;

import com.ssafy.seoulpop.member.domain.Member;
import com.ssafy.seoulpop.notification.dto.CookieRequestDto;
import com.ssafy.seoulpop.notification.dto.NotificationRequestDto;
import com.ssafy.seoulpop.notification.dto.NotificationResponseDto;
import com.ssafy.seoulpop.notification.dto.UpdateRequestDto;
import com.ssafy.seoulpop.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "푸시알림 컨트롤러", description = "FCM토큰 쿠키 발급, 주변 역사 정보 알림 전송 기능이 포함되어 있음")
@RestController
@RequestMapping("v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(
        summary = "쿠키 발급",
        description = "fcmToken 이름으로 쿠키 발급"
    )
    @PostMapping("/regist")
    public ResponseEntity<String> addCookie(HttpServletResponse response, @RequestBody CookieRequestDto requestDto) {
        return ResponseEntity.ok(notificationService.createCookie(response, requestDto));
    }

    @Operation(
        summary = "알림 확인 및 생성",
        description = "사용자 위치 기반 알림 확인, 가장 가까운 역사 알림 생성"
    )
    @PostMapping("/send")
    public ResponseEntity<String> addNotification(@AuthenticationPrincipal Member member, HttpServletRequest request, @RequestBody NotificationRequestDto requestDto) throws IOException {
        return ResponseEntity.ok(notificationService.sendNotification(member, request, requestDto));
    }

    @Operation(
        summary = "전체 알림 내역 조회",
        description = "사용자별 알림 내역 조회, List로 반환"
    )
    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getNotificationList(@AuthenticationPrincipal Member member, HttpServletRequest request) {
        return ResponseEntity.ok(notificationService.readNotificationList(member.getId()));
    }

    @Operation(
            summary = "알림 확인 상태로 변경",
            description = "미확인 알림을 확인 알림으로 변경"
    )
    @PatchMapping
    public ResponseEntity<String> editNotification(@RequestBody UpdateRequestDto requestDto) {
        return ResponseEntity.ok(notificationService.updateNotification(requestDto));
    }
}
