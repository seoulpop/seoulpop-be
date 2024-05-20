package com.ssafy.seoulpop.history.dto;

import com.ssafy.seoulpop.history.domain.History;
import com.ssafy.seoulpop.image.dto.ImageDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record HeritageDto(
    String category,
    Long id,
    Double lat,
    Double lng,
    String thumbnail,
    String name,
    String label,
    LocalDate registeredAt,
    String description,
    String additionalInfo,
    String era,
    String address,
    String atlasImageUrl,
    List<ImageDto> images
) {

    public static HeritageDto from(History history) {
        return HeritageDto.builder()
            .category(history.getCategory())
            .id(history.getId())
            .lat(history.getLat())
            .lng(history.getLng())
            .thumbnail(history.getThumbnail())
            .name(history.getName())
            .label(history.getLabel())
            .registeredAt(history.getRegisteredAt())
            .description(history.getDescription())
            .additionalInfo(history.getAdditionalInfo())
            .atlasImageUrl(history.getAtlasImageUrl())
            .era(history.getEra())
            .address(history.getAddress())
            .images(history.getImages().stream().map(ImageDto::from).toList())
            .build();
    }
}
