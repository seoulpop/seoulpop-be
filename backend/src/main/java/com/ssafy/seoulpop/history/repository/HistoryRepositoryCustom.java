package com.ssafy.seoulpop.history.repository;

import com.ssafy.seoulpop.history.domain.History;
import java.util.List;

public interface HistoryRepositoryCustom {

    List<History> findByCellList(int level, List<String> cellList);

}
