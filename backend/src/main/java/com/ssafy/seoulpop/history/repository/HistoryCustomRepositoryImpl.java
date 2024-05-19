package com.ssafy.seoulpop.history.repository;

import static com.ssafy.seoulpop.atlas.domain.QAtlas.atlas;
import static com.ssafy.seoulpop.history.domain.QHistory.history;
import static com.ssafy.seoulpop.image.domain.QImage.image;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.seoulpop.history.domain.History;
import com.ssafy.seoulpop.history.dto.HistoryMapResponseDto;
import com.ssafy.seoulpop.history.dto.NearByArResponseDto;
import com.ssafy.seoulpop.history.dto.NearByHistoryResponseDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HistoryCustomRepositoryImpl implements HistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NearByHistoryResponseDto> findByCell(Integer level, List<String> cellList) {
        String cellIndexField = getCellIndexField(level);
        return nearByQuery(cellList, cellIndexField);
    }

    @Override
    public List<NearByArResponseDto> findByCellList(Integer level, List<String> cellList) {
        String cellIndexField = getCellIndexField(level);
        return arQuery(cellList, cellIndexField);
    }

    @Override
    public List<HistoryMapResponseDto> findAllHistories() {
        return queryFactory.select(Projections.constructor(HistoryMapResponseDto.class,
                history.id,
                history.lat,
                history.lng,
                history.name,
                history.category,
                Expressions.constant(false)))
            .from(history)
            .leftJoin(atlas).on(atlas.history.id.eq(history.id))
            .orderBy(history.category.asc())
            .fetch();
    }

    @Override
    public List<HistoryMapResponseDto> findAllByCategory(String category) {
        return queryFactory.select(Projections.constructor(HistoryMapResponseDto.class,
                history.id,
                history.lat,
                history.lng,
                history.name,
                history.category,
                Expressions.constant(false)))
            .from(history)
            .leftJoin(atlas).on(atlas.history.id.eq(history.id))
            .where(history.category.eq(category))
            .orderBy(history.category.asc())
            .fetch();
    }

    @Override
    public Optional<History> findByIdWithImages(Long historyId) {
        History historyDetail = queryFactory
                .selectFrom(history)
                .leftJoin(history.images, image).fetchJoin()
                .where(history.id.eq(historyId))
                .fetchOne();

        return Optional.ofNullable(historyDetail);
    }

    private String getCellIndexField(int level) {
        return "cell" + level + "Index";
    }

    private List<NearByHistoryResponseDto> nearByQuery(List<String> cellList, String cellIndexField) {
        StringPath cellIndexPath = Expressions.stringPath(history.cell, cellIndexField);
        return queryFactory.select(Projections.constructor(NearByHistoryResponseDto.class,
                history.id,
                history.lat,
                history.lng,
                history.name,
                history.category,
                history.thumbnail,
                history.address,
                Expressions.constant(false)))
            .from(history)
            .leftJoin(atlas).on(atlas.history.id.eq(history.id))
            .where(cellIndexPath.in(cellList))
            .orderBy(history.category.asc())
            .fetch();
    }

    private List<NearByArResponseDto> arQuery(List<String> cellList, String cellIndexField) {
        StringPath cellIndexPath = Expressions.stringPath(history.cell, cellIndexField);
        return queryFactory.select(Projections.constructor(NearByArResponseDto.class,
                history.id,
                history.name,
                history.category,
                history.lat,
                history.lng,
                history.arImage
            ))
            .from(history)
            .where(cellIndexPath.in(cellList))
            .orderBy(history.category.asc())
            .fetch();
    }
}
