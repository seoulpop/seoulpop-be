package com.ssafy.seoulpop.atlas.dto;

import lombok.Builder;

@Builder
public record AtlasInfoResponseDto(
    Long historyId,
    String historyCategory,
    String historyName,
    String heritageImgUrl,
    Boolean visited
) {

}
