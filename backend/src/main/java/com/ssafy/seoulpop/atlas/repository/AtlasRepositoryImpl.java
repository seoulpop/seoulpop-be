package com.ssafy.seoulpop.atlas.repository;

import static com.ssafy.seoulpop.atlas.domain.QAtlas.atlas;
import static com.ssafy.seoulpop.history.domain.QHistory.history;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
public class AtlasRepositoryImpl implements AtlasRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Tuple> findHistoryAndAtlas(long memberId) {
        return jpaQueryFactory.select(history.id, history.category, history.name, history.atlasImageUrl, atlas.id)
            .leftJoin(atlas).on(atlas.history.id.eq(history.id).and(atlas.member.id.eq(memberId)))
            .orderBy(history.category.asc())
            .fetch();
    }
}
