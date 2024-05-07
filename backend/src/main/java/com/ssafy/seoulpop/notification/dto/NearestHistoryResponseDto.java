package com.ssafy.seoulpop.notification.dto;

import lombok.Builder;

@Builder
public record NearestHistoryResponseDto(
        Long historyId,
        String name,
        String category,
        Integer distance
) {

}
