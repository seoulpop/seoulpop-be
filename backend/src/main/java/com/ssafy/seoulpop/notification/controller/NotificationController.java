package com.ssafy.seoulpop.notification.controller;

import com.ssafy.seoulpop.notification.dto.CookieRequestDto;
import com.ssafy.seoulpop.notification.dto.NotificationRequestDto;
import com.ssafy.seoulpop.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "푸시알림 컨트롤러", description = "FCM토큰 쿠키 발급, 주변 역사 정보 알림 전송 기능이 포함되어 있음")
@RestController
@RequestMapping("v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService fcmService;

    @Operation(
        summary = "쿠키 발급",
        description = "fcmToken 이름으로 쿠키 발급"
    )
    @PostMapping("/regist")
    public ResponseEntity<String> getCookie(HttpServletResponse response, @RequestBody CookieRequestDto requestDto) {
        return ResponseEntity.ok(fcmService.createCookie(response, requestDto));
    }

    @Operation(
        summary = "알림 확인 및 생성",
        description = "사용자 위치 기반 알림 확인, 가장 가까운 역사 알림 생성"
    )
    @PostMapping
    public ResponseEntity<String> getNotification(HttpServletRequest request, @RequestBody NotificationRequestDto requestDto) throws IOException {
        return ResponseEntity.ok(fcmService.sendNotification(request, requestDto));
    }
}
