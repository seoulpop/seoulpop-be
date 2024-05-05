package com.ssafy.seoulpop.history.dto;

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

}
