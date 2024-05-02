package com.ssafy.seoulpop.history.dto;

import com.ssafy.seoulpop.history.domain.History;
import lombok.Builder;

@Builder
public record NearByHistoryResponseDto(
    Long id,
    Double lat,
    Double lng,
    String name,
    String category,
    String thumbnail,
    String address,
    Boolean visited
) {
    public static NearByHistoryResponseDto from(History history) {
        return NearByHistoryResponseDto.builder()
            .id(history.getId())
            .lat(history.getLat())
            .lng(history.getLng())
            .name(history.getName())
            .category(history.getCategory())
            .thumbnail(history.getThumbnail())
            .address(history.getAddress())
            .build();
    }
}
