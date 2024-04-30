package com.ssafy.seoulpop.atlas.controller;

import com.ssafy.seoulpop.atlas.dto.AtlasInfoResponse;
import com.ssafy.seoulpop.atlas.service.AtlasService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atlases")
@RequiredArgsConstructor
public class AtlasController {

    //TODO: 회원 연동

    private final AtlasService atlasService;

    @GetMapping
    public ResponseEntity<List<AtlasInfoResponse>> getAtlas(Long memberId) {
        return ResponseEntity.ok(atlasService.readAtlas(memberId));
    }
}
