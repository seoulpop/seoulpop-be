package com.ssafy.seoulpop.history.service;

import com.ssafy.seoulpop.history.dto.HistoryMapResponseDto;
import com.ssafy.seoulpop.history.dto.NearByHistoryResponseDto;
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

    public List<HistoryMapResponseDto> readHistoryList() {
        return historyRepository.findAll().stream().map(HistoryMapResponseDto::from).toList();
    }

    public List<HistoryMapResponseDto> readHistoryList(String category) {
        return historyRepository.findAllByCategory(category).stream().map(HistoryMapResponseDto::from).toList();
    }

    public List<NearByHistoryResponseDto> readNearByHistoryList(double lat, double lng, int level) {
        // 위 경도에서 셀 Index로 변환
        String cellIndex = h3.latLngToCellAddress(lat, lng, level);
        // 주변 셀 List 확보
        List<String> cellList = new ArrayList<>();
        cellList.addAll(0, h3.gridDisk(cellIndex, 1));

        return historyRepository.findByCellList(level, cellList).stream().map(NearByHistoryResponseDto::from).toList();
    }
}
