package com.ssafy.seoulpop.notification.dto;

public record NotificationRequestDto(String fcmToken, Double latitude, Double longitude) {
}
