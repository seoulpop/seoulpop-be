package com.ssafy.seoulpop.notification.controller;

import com.ssafy.seoulpop.notification.dto.NotificationRequest;
import com.ssafy.seoulpop.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<String> getAlert(@RequestBody NotificationRequest notificationRequest) {
        return ResponseEntity.ok(notificationService.createAlert(notificationRequest));
    }
}
