package com.ssafy.seoulpop.history.dto;

import com.ssafy.seoulpop.history.domain.History;
import lombok.Builder;

@Builder
public record SiteDto(
    String category,
    Long id,
    String thumbnail,
    String label,
    String status,
    String summary,
    String description,
    String historicAddress,
    String address,
    String reference
) {

    public static SiteDto from(History history) {
        return SiteDto.builder()
            .category(history.getCategory())
            .id(history.getId())
            .thumbnail(history.getThumbnail())
            .label(history.getLabel())
            .status(history.getStatus())
            .summary(history.getSummary())
            .description(history.getDescription())
            .historicAddress(history.getHistoricAddress())
            .address(history.getAddress())
            .reference(history.getReference())
            .build();
    }
}
