package com.ssafy.seoulpop.history.dto;

import com.ssafy.seoulpop.history.domain.History;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record HeritageDto(
    String category,
    Long id,
    String thumbnail,
    String label,
    LocalDateTime registeredAt,
    String description,
    String era,
    String address
) {

    public static HeritageDto from(History history) {
        return HeritageDto.builder()
            .category(history.getCategory())
            .id(history.getId())
            .thumbnail(history.getThumbnail())
            .label(history.getLabel())
            .registeredAt(history.getRegisteredAt())
            .description(history.getDescription())
            .era(history.getEra())
            .address(history.getAddress())
            .build();
    }
}
