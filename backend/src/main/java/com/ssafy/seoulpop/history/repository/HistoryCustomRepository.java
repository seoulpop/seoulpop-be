package com.ssafy.seoulpop.history.repository;

import com.ssafy.seoulpop.history.dto.HistoryMapResponseDto;
import com.ssafy.seoulpop.history.dto.NearByHistoryResponseDto;
import java.util.List;

public interface HistoryCustomRepository {

    List<NearByHistoryResponseDto> findByCellList(Long memberId, int level, List<String> cellList);

    List<HistoryMapResponseDto> findAllByMemberId(Long memberId);

    List<HistoryMapResponseDto> findAllByMemberIdAndCategory(Long memberId, String category);

}
