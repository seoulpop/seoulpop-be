package com.ssafy.seoulpop.notification.controller;

import com.ssafy.seoulpop.notification.dto.FcmCookieRequestDto;
import com.ssafy.seoulpop.notification.dto.NotificationRequestDto;
import com.ssafy.seoulpop.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(notificationService.createNotification(request, requestDto));
    }
}
