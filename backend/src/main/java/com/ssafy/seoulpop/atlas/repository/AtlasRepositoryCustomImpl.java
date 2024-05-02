package com.ssafy.seoulpop.atlas.repository;

import static com.ssafy.seoulpop.atlas.domain.QAtlas.atlas;
import static com.ssafy.seoulpop.history.domain.QHistory.history;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.seoulpop.atlas.dto.AtlasInfoResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtlasRepositoryCustomImpl implements AtlasRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<AtlasInfoResponseDto> findAtlasInfoByMemberId(long memberId) {
        return jpaQueryFactory.select(Projections.constructor(AtlasInfoResponseDto.class,
                history.id,
                history.category,
                history.name,
                history.atlasImageUrl,
                new CaseBuilder().when(atlas.id.isNull()).then(false).otherwise(true)))
            .from(history)
            .leftJoin(atlas).on(atlas.history.id.eq(history.id).and(atlas.member.id.eq(memberId)))
            .orderBy(history.category.asc())
            .fetch();
    }
}
