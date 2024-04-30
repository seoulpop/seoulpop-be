package com.ssafy.seoulpop.history.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.seoulpop.history.domain.History;
import com.ssafy.seoulpop.history.domain.QHistory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HistoryCustomRepositoryImpl implements HistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<History> findByCellList(int level, List<String> cellList) {
        QHistory history = QHistory.history;
        return switch (level) {
            case 7 -> queryFactory.selectFrom(history)
                .where(history.cell.cell7Index.in(cellList))
                .fetch();
            case 8 -> queryFactory.selectFrom(history)
                .where(history.cell.cell8Index.in(cellList))
                .fetch();
            case 9 -> queryFactory.selectFrom(history)
                .where(history.cell.cell9Index.in(cellList))
                .fetch();
            case 10 -> queryFactory.selectFrom(history)
                .where(history.cell.cell10Index.in(cellList))
                .fetch();
            default -> queryFactory.selectFrom(history)
                .where(history.cell.cell11Index.in(cellList))
                .fetch();
        };
    }
}
