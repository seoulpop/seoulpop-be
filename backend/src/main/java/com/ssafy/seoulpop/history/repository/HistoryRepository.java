package com.ssafy.seoulpop.history.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.seoulpop.history.domain.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
}
