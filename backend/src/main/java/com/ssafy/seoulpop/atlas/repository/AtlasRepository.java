package com.ssafy.seoulpop.atlas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.seoulpop.atlas.domain.Atlas;

@Repository
public interface AtlasRepository extends JpaRepository<Atlas, Long> {
}
