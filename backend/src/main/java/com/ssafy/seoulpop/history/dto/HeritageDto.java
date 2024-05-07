package com.ssafy.seoulpop.history.dto;

import com.ssafy.seoulpop.history.domain.History;
import com.ssafy.seoulpop.image.dto.ImageDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record HeritageDto(
    String category,
    Long id,
    String thumbnail,
    String name,
    String label,
    LocalDateTime registeredAt,
    String description,
    String additionalInfo,
    String era,
    String address,
    List<ImageDto> images
) {

    public static HeritageDto from(History history) {
        return HeritageDto.builder()
            .category(history.getCategory())
            .id(history.getId())
            .thumbnail(history.getThumbnail())
            .name(history.getName())
            .label(history.getLabel())
            .registeredAt(history.getRegisteredAt())
            .description(history.getDescription())
            .additionalInfo(history.getAdditionalInfo())
            .era(history.getEra())
            .address(history.getAddress())
            .images(history.getImages().stream().map(ImageDto::from).toList())
            .build();
    }
}
