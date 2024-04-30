package com.ssafy.seoulpop.atlas.controller;

import com.ssafy.seoulpop.atlas.dto.AtlasInfoResponse;
import com.ssafy.seoulpop.atlas.service.AtlasService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/atlases")
@RequiredArgsConstructor
public class AtlasController {

    private final AtlasService atlasService;

    @Operation(
        summary = "도감 조회",
        description = "회원별 도감 정보를 조회(RequestParam: 회원 아이디(임시))"
    )
    @GetMapping
    public ResponseEntity<List<AtlasInfoResponse>> getAtlas(@RequestParam Long memberId) {
        return ResponseEntity.ok(atlasService.readAtlas(memberId));
    }

    @Operation(
        summary = "도감 등록",
        description = "AR 인식이 완료된 역사를 도감에 등록합니다(RequestParam: 회원 아이디(임시), 역사 아이디)"
    )
    @PostMapping
    public ResponseEntity<Void> addAtlas(@RequestParam Long memberId, @RequestParam Long historyId) {
        atlasService.createAtlas(memberId, historyId);
        return ResponseEntity.ok().build();
    }
}
