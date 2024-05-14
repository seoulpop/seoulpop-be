package com.ssafy.seoulpop.history.dto;

public record NearByArResponseDto(
    Long id,
    String name,
    String category,
    Double lat,
    Double lng,
    String arImage
) {

}
