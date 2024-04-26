package com.ssafy.seoulpop.history.service;

import com.ssafy.seoulpop.history.dto.HistoryMapResponseDto;
import com.ssafy.seoulpop.history.repository.HistoryRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    public List<HistoryMapResponseDto> readHistoryList() {
        return historyRepository.findAll().stream().map(HistoryMapResponseDto::from).toList();
    }

    public List<HistoryMapResponseDto> readHistoryList(String category) {
        return historyRepository.findByCategory(category).stream().map(HistoryMapResponseDto::from).toList();
    }
}
