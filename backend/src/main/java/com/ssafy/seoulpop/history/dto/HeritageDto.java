package com.ssafy.seoulpop.history.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record HeritageDto(
    String type,
    String label,
    LocalDateTime registeredAt,
    String description,
    String era,
    String address
) {

}
