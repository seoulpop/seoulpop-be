package com.ssafy.seoulpop.atlas.dto;

import lombok.Builder;

@Builder
public record AtlasInfoResponse(
    Long historyId,
    String historyCategory,
    String historyName,
    String heritageImgUrl,
    Boolean visited
) {

}
