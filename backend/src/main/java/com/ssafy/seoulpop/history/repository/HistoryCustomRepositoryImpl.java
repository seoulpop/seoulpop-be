package com.ssafy.seoulpop.history.repository;

import static com.ssafy.seoulpop.atlas.domain.QAtlas.atlas;
import static com.ssafy.seoulpop.history.domain.QHistory.history;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.seoulpop.history.dto.HistoryMapResponseDto;
import com.ssafy.seoulpop.history.dto.NearByHistoryResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HistoryCustomRepositoryImpl implements HistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NearByHistoryResponseDto> findByCellList(Long memberId, int level, List<String> cellList) {
        String cellIndexField = getCellIndexField(level);
        return cellIndexQuery(memberId, cellList, cellIndexField);
    }

    private String getCellIndexField(int level) {
        return "cell" + level + "Index";
    }

    private List<NearByHistoryResponseDto> cellIndexQuery(Long memberId, List<String> cellList, String cellIndexField) {
        StringPath cellIndexPath = Expressions.stringPath(history.cell, cellIndexField);
        return queryFactory.select(Projections.constructor(NearByHistoryResponseDto.class,
                history.id,
                history.lat,
                history.lng,
                history.name,
                history.category,
                history.thumbnail,
                history.address,
                new CaseBuilder().when(atlas.id.isNull()).then(false).otherwise(true)))
            .from(history)
            .leftJoin(atlas).on(atlas.history.id.eq(history.id).and(atlas.member.id.eq(memberId)))
            .where(cellIndexPath.in(cellList))
            .orderBy(history.category.asc())
            .fetch();
    }

    @Override
    public List<HistoryMapResponseDto> findAllByMemberId(Long memberId) {
        return queryFactory.select(Projections.constructor(HistoryMapResponseDto.class,
                history.id,
                history.lat,
                history.lng,
                history.name,
                history.category,
                new CaseBuilder().when(atlas.id.isNull()).then(false).otherwise(true)))
            .from(history)
            .leftJoin(atlas).on(atlas.history.id.eq(history.id).and(atlas.member.id.eq(memberId)))
            .orderBy(history.category.asc())
            .fetch();
    }

    @Override
    public List<HistoryMapResponseDto> findAllByMemberIdAndCategory(Long memberId, String category) {
        return queryFactory.select(Projections.constructor(HistoryMapResponseDto.class,
                history.id,
                history.lat,
                history.lng,
                history.name,
                history.category,
                new CaseBuilder().when(atlas.id.isNull()).then(false).otherwise(true)))
            .from(history)
            .leftJoin(atlas).on(atlas.history.id.eq(history.id).and(atlas.member.id.eq(memberId)))
            .where(history.category.eq(category))
            .orderBy(history.category.asc())
            .fetch();
    }
}
