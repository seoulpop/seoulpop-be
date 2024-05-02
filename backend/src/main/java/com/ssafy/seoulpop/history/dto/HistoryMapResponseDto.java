package com.ssafy.seoulpop.history.dto;

import com.ssafy.seoulpop.history.domain.History;
import lombok.Builder;

@Builder
public record HistoryMapResponseDto(
    Long id,
    Double lat,
    Double lng,
    String name,
    String category,
    Boolean visited
) {

}
