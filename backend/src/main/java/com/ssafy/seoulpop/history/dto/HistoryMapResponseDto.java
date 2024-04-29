package com.ssafy.seoulpop.history.dto;

import com.ssafy.seoulpop.history.domain.History;
import lombok.Builder;

@Builder
public record HistoryMapResponseDto(
    Long id,
    Double lat,
    Double lng,
    String name,
    String category
) {

    public static HistoryMapResponseDto from(History history) {
        return HistoryMapResponseDto.builder()
            .id(history.getId())
            .lat(history.getLat())
            .lng(history.getLng())
            .name(history.getName())
            .category(history.getCategory())
            .build();
    }
}
