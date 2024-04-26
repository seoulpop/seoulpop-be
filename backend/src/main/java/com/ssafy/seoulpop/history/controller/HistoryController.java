package com.ssafy.seoulpop.history.controller;

import com.ssafy.seoulpop.history.dto.HistoryMapResponseDto;
import com.ssafy.seoulpop.history.service.HistoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/maps")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<HistoryMapResponseDto>> getHistoryList(@RequestParam(required = false) String category) {
        if (category == null) {
            return ResponseEntity.ok(historyService.readHistoryList());
        }
        return ResponseEntity.ok(historyService.readHistoryList(category));
    }
}
