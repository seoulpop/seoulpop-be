package com.ssafy.seoulpop.notification.controller;

import com.ssafy.seoulpop.notification.dto.FcmCookieRequestDto;
import com.ssafy.seoulpop.notification.dto.NotificationRequestDto;
import com.ssafy.seoulpop.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> getCookie(HttpServletResponse response, @RequestBody FcmCookieRequestDto requestDto) {
        return ResponseEntity.ok(notificationService.createCookie(response, requestDto));
    }

    @Operation(
        summary = "알림 확인 및 생성",
        description = "사용자 위치 기반 알림 확인, 가장 가까운 역사 알림 생성"
    )
    @PostMapping
    public ResponseEntity<String> getNotification(HttpServletRequest request, @RequestBody NotificationRequestDto requestDto) {
        return ResponseEntity.ok(notificationService.sendNotification(request, requestDto));
    }

    @Operation(
            summary = "알림 내용 조회",
            description = "사용자가 선택한 푸시 알림 내용 반환"
    )
    @GetMapping("/{notificationId}")
    public ResponseEntity<String> getNotificationInfo(@PathVariable String notificationId) {
        return ResponseEntity.ok(notificationService.readNotificationInfo(notificationId));
    }
}
