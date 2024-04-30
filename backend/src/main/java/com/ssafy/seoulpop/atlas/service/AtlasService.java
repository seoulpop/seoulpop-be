package com.ssafy.seoulpop.atlas.service;

import static com.ssafy.seoulpop.atlas.domain.QAtlas.atlas;
import static com.ssafy.seoulpop.history.domain.QHistory.history;

import com.querydsl.core.Tuple;
import com.ssafy.seoulpop.atlas.dto.AtlasInfoResponse;
import com.ssafy.seoulpop.atlas.repository.AtlasRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtlasService {

    private final AtlasRepository atlasRepository;

    public List<AtlasInfoResponse> readAtlas(Long memberId) {
        List<Tuple> findResult = atlasRepository.findHistoryAndAtlas(memberId);

        List<AtlasInfoResponse> atlasInfoList = new ArrayList<>();
        for (Tuple tuple : findResult) {
            AtlasInfoResponse atlasInfo = AtlasInfoResponse.builder()
                .historyId(tuple.get(history.id))
                .historyCategory(tuple.get(history.category))
                .historyName(tuple.get(history.name))
                .heritageImgUrl(tuple.get(history.atlasImageUrl))
                .visited(tuple.get(atlas.id) != null)
                .build();

            atlasInfoList.add(atlasInfo);
        }

        return atlasInfoList;
    }
}
