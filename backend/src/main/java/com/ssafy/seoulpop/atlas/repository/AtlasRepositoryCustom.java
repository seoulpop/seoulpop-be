package com.ssafy.seoulpop.atlas.repository;

import com.ssafy.seoulpop.atlas.dto.AtlasInfoResponseDto;
import java.util.List;

public interface AtlasRepositoryCustom {

    List<AtlasInfoResponseDto> findAtlasInfoByMemberId(long memberId);
}
