package com.ssafy.seoulpop.notification.dto;

public record NotificationRequestDto(Long memberId, String fcmToken, Double latitude, Double longitude) {

}
