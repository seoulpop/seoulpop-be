package com.ssafy.seoulpop.heritage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.seoulpop.heritage.domain.Heritage;

@Repository
public interface HeritageRepository extends JpaRepository<Heritage, Long> {
}
