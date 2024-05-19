package com.ssafy.seoulpop.atlas.controller;

import com.ssafy.seoulpop.atlas.dto.AtlasInfoResponseDto;
import com.ssafy.seoulpop.atlas.dto.AtlasRegistRequestDto;
import com.ssafy.seoulpop.atlas.service.AtlasService;
import com.ssafy.seoulpop.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "도감 컨트롤러", description = "회원별 도감 정보 조회, 도감 등록 기능이 포함되어 있다")
@RestController
@RequestMapping("v1/atlases")
@RequiredArgsConstructor
public class AtlasController {

    private final AtlasService atlasService;

    @Operation(
        summary = "도감 조회",
        description = "회원별 도감 정보를 조회"
    )
    @GetMapping("/member")
    public ResponseEntity<List<AtlasInfoResponseDto>> getAtlas(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(atlasService.readAtlas(member.getId()));
    }

    @Operation(
        summary = "도감 등록",
        description = "AR 인식이 완료된 역사를 도감에 등록합니다(추가되면 true 반환)"
    )
    @PostMapping
    public ResponseEntity<Boolean> addAtlas(@AuthenticationPrincipal Member member, @RequestBody AtlasRegistRequestDto requestDto) {
        return ResponseEntity.ok(atlasService.createAtlas(member.getId(), requestDto.historyId()));
    }

    @Operation(
        summary = "도감 전체 리스트 조회",
        description = "도감 이미지 전체를 가져온다."
    )
    @GetMapping
    public ResponseEntity<List<AtlasInfoResponseDto>> getAtlasList() {
        return ResponseEntity.ok(atlasService.readAtlasList());
    }
}
