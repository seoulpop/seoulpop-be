package com.ssafy.seoulpop.history.dto;

import lombok.Builder;

@Builder
public record SiteDto(

    String label,
    String status,
    String summary,
    String description,
    String historicAddress,
    String address,
    String reference
) {

}
