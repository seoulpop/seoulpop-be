package com.ssafy.seoulpop.notification.dto;

public record NotificationRequest(String deviceToken, Double latitude, Double longitude) {
}
