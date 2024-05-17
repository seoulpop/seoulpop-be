package com.ssafy.seoulpop.atlas.service;

import com.ssafy.seoulpop.atlas.domain.Atlas;
import com.ssafy.seoulpop.atlas.dto.AtlasInfoResponseDto;
import com.ssafy.seoulpop.atlas.repository.AtlasRepository;
import com.ssafy.seoulpop.exception.BaseException;
import com.ssafy.seoulpop.exception.ErrorCode;
import com.ssafy.seoulpop.history.domain.History;
import com.ssafy.seoulpop.history.repository.HistoryRepository;
import com.ssafy.seoulpop.member.domain.Member;
import com.ssafy.seoulpop.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AtlasService {

    private final AtlasRepository atlasRepository;
    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;

    public List<AtlasInfoResponseDto> readAtlas(Long memberId) {
        return atlasRepository.findAtlasInfoByMemberId(memberId);
    }

    @Transactional
    public void createAtlas(Long memberId, Long historyId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new BaseException(ErrorCode.MEMBER_NOT_FOUND_ERROR));
        History findHistory = historyRepository.findById(historyId).orElseThrow(() -> new BaseException(ErrorCode.HISTORY_NOT_FOUND_ERROR));

        Optional<Atlas> findAtlas = atlasRepository.findByMemberIdAndHistoryId(memberId, historyId);

        if (findAtlas.isEmpty()) {
            atlasRepository.save(Atlas.builder()
                .member(findMember)
                .history(findHistory)
                .visitCnt(1)
                .build());
            return;
        }

        Atlas atlas = findAtlas.get();
        atlas.updateVisitCnt();
    }
}
