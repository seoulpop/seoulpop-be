package com.ssafy.seoulpop.history.service;

import com.ssafy.seoulpop.exception.BaseException;
import com.ssafy.seoulpop.exception.ErrorCode;
import com.ssafy.seoulpop.history.domain.History;
import com.ssafy.seoulpop.history.dto.HeritageDto;
import com.ssafy.seoulpop.history.dto.HistoryMapResponseDto;
import com.ssafy.seoulpop.history.dto.NearByArResponseDto;
import com.ssafy.seoulpop.history.dto.NearByHistoryResponseDto;
import com.ssafy.seoulpop.history.dto.SiteDto;
import com.ssafy.seoulpop.history.repository.HistoryRepository;
import com.uber.h3core.H3Core;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    private H3Core h3;

    @PostConstruct
    private void h3Init() throws Exception {
        h3 = H3Core.newInstance();
    }

    public List<HistoryMapResponseDto> readHistoryList(Long memberId) {
        return historyRepository.findAllByMemberId(memberId);
    }

    public List<HistoryMapResponseDto> readHistoryList(Long memberId, String category) {
        return historyRepository.findAllByMemberIdAndCategory(memberId, category);
    }

    public List<NearByHistoryResponseDto> readNearByHistoryList(Long memberId, double lat, double lng, int level) {
        List<String> cellList = getCellList(lat, lng, level);
        return historyRepository.findByMemberIdAndCellList(memberId, level, cellList);
    }

    public Object readHistoryDetail(Long historyId) {
        History historyDetail = historyRepository.findById(historyId)
            .orElseThrow(() -> new BaseException(ErrorCode.HISTORY_NOT_FOUND_ERROR));
        if (historyDetail.getCategory().equals("문화재")) {
            return HeritageDto.from(historyDetail);
        }
        return SiteDto.from(historyDetail);
    }

    public List<NearByArResponseDto> readNearByArMarker(double lat, double lng, int level) {
        List<String> cellList = getCellList(lat, lng, level);
        return historyRepository.findByCellList(level, cellList);
    }

    private List<String> getCellList(double lat, double lng, int level) {
        // 위 경도에서 셀 Index로 변환
        String cellIndex = h3.latLngToCellAddress(lat, lng, level);
        // 주변 셀 List 확보
        return new ArrayList<>(h3.gridDisk(cellIndex, 1));
    }
}
