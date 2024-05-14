package com.ssafy.seoulpop.notification.dto;

import lombok.Builder;

@Builder
public record NotificationResponseDto(
    String notificationId,
    Long historyId,
    String historyName,
    String historyCategory,
    Double historyLat,
    Double historyLng,
    String title,
    String body,
    Boolean checked
) {

}
