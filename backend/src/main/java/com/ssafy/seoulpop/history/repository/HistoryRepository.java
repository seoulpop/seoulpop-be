package com.ssafy.seoulpop.history.repository;

import com.ssafy.seoulpop.history.domain.History;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long>, HistoryCustomRepository {

    List<History> findAllByCategory(String category);
}
