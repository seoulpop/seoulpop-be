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
import com.ssafy.seoulpop.member.repository.MemberRepository;
import com.uber.h3core.H3Core;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final MemberRepository memberRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private H3Core h3;

    @PostConstruct
    private void h3Init() throws Exception {
        h3 = H3Core.newInstance();
    }

    public List<HistoryMapResponseDto> readHistoryList(Long memberId) {
        checkMemberId(memberId);
        return historyRepository.findAllByMemberId(memberId);
    }

    public List<HistoryMapResponseDto> readHistoryList(Long memberId, String category) {
        checkMemberId(memberId);
        //TODO: category Enum처리 후 검증 로직 추가
        return historyRepository.findAllByMemberIdAndCategory(memberId, category);
    }

    public List<NearByHistoryResponseDto> readNearByHistoryList(Long memberId, Double lat, Double lng, Integer level) {
        checkMemberId(memberId);
        checkLocation(lat, lng);
        checkLevel(level);
        List<String> cellList = getCellList(lat, lng, level);
        return historyRepository.findByMemberIdAndCellList(memberId, level, cellList);
    }


    public Object readHistoryDetail(Long historyId) {
        History historyDetail = historyRepository.findByIdWithImages(historyId)
            .orElseThrow(() -> new BaseException(ErrorCode.HISTORY_NOT_FOUND_ERROR));

        log.info("\n [해당 이미지 수] \n {}", historyDetail.getImages().size());

        if (historyDetail.getCategory().equals("문화재")) {
            return HeritageDto.from(historyDetail);
        }
        return SiteDto.from(historyDetail);
    }

    public List<NearByArResponseDto> readNearByArMarker(Double lat, Double lng, Integer level) {
        checkLocation(lat, lng);
        checkLevel(level);
        List<String> cellList = getCellList(lat, lng, level);
        return historyRepository.findByCellList(level, cellList);
    }

    private List<String> getCellList(Double lat, Double lng, Integer level) {
        // 위 경도에서 셀 Index로 변환
        String cellIndex = h3.latLngToCellAddress(lat, lng, level);
        // 주변 셀 List 확보
        return new ArrayList<>(h3.gridDisk(cellIndex, 1));
    }

    private void checkMemberId(Long memberId) {
        memberRepository.findById(memberId).orElseThrow(
            () -> new BaseException(ErrorCode.MEMBER_NOT_FOUND_ERROR)
        );
    }

    private void checkLocation(Double lat, Double lng) {
        if (lat == null || lng == null) {
            throw new BaseException(ErrorCode.INVALID_LOCATION_ERROR);
        }
        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
            throw new BaseException(ErrorCode.INVALID_LOCATION_ERROR);
        }
    }

    private void checkLevel(Integer level) {
        if (level == null) {
            throw new BaseException(ErrorCode.INVALID_LEVEL_ERROR);
        }
        if (level < 7 || level > 13) {
            throw new BaseException(ErrorCode.INVALID_LEVEL_ERROR);
        }
    }
}
