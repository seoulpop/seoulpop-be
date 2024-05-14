package com.ssafy.seoulpop.notification.dto;

public record NotificationRequestDto(
    Long memberId,
    Double lat,
    Double lng) {

}
