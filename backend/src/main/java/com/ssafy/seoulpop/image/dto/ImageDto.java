package com.ssafy.seoulpop.image.dto;

import com.ssafy.seoulpop.image.domain.Image;
import lombok.Builder;

@Builder
public record ImageDto(
    String imageUrl,
    String caption
) {
    public static ImageDto from(Image image) {
        return ImageDto.builder()
            .imageUrl(image.getImageUrl())
            .caption(image.getCaption())
            .build();
    }
}
