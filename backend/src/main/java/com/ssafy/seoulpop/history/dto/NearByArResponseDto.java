package com.ssafy.seoulpop.history.dto;

public record NearByArResponseDto(
    Long id,
    Double lat,
    Double lng,
    String arImage
) {

}
