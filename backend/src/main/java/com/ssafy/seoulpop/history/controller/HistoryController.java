package com.ssafy.seoulpop.history.controller;

import com.ssafy.seoulpop.history.dto.HistoryMapResponseDto;
import com.ssafy.seoulpop.history.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "역사관련 컨트롤러", description = "전체 역사 조회, 주변 역사 조회, 필터 기반 조회 기능이 포함되어 있음")
@RestController
@RequestMapping("/v1/histories")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @Operation(
        summary = "역사조회",
        description = "역사를 조회하며 카테고리기반 필터링이 가능함"
    )
    @GetMapping
    public ResponseEntity<List<HistoryMapResponseDto>> getHistoryList(@RequestParam(required = false) String category) {
        if (category == null) {
            return ResponseEntity.ok(historyService.readHistoryList());
        }
        return ResponseEntity.ok(historyService.readHistoryList(category));
    }
}