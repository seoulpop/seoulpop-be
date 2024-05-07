package com.ssafy.seoulpop.history.dto;

import com.ssafy.seoulpop.history.domain.History;
import com.ssafy.seoulpop.image.dto.ImageDto;
import java.util.List;
import lombok.Builder;


@Builder
public record SiteDto(
    String category,
    Long id,
    String thumbnail,
    String name,
    String label,
    String status,
    String summary,
    String description,
    String historicAddress,
    String address,
    String reference,
    List<ImageDto> images
) {

    public static SiteDto from(History history) {
        return SiteDto.builder()
            .category(history.getCategory())
            .id(history.getId())
            .thumbnail(history.getThumbnail())
            .name(history.getName())
            .label(history.getLabel())
            .status(history.getStatus())
            .summary(history.getSummary())
            .description(history.getDescription())
            .historicAddress(history.getHistoricAddress())
            .address(history.getAddress())
            .reference(history.getReference())
            .images(history.getImages().stream().map(ImageDto::from).toList())
            .build();
    }
}
