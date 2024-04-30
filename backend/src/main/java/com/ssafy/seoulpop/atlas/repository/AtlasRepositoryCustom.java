package com.ssafy.seoulpop.atlas.repository;

import com.querydsl.core.Tuple;
import java.util.List;
import org.springframework.stereotype.Repository;

public interface AtlasRepositoryCustom {

    List<Tuple> findHistoryAndAtlas(long memberId);
}
