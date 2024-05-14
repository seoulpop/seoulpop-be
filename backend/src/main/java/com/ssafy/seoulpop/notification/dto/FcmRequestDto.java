package com.ssafy.seoulpop.notification.dto;

import lombok.Builder;

@Builder
public record FcmRequestDto(
    Message message
) {

    @Builder
    public record Message(
        String token,
        Data data,
        Notification notification
    ) {

    }

    @Builder
    public record Data(
        String notificationId,
        String historyId,
        String historyName,
        String historyCategory,
        String historyLat,
        String historyLng
    ) {

    }

    @Builder
    public record Notification(
        String body,
        String title
    ) {

    }
}
