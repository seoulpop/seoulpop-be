package com.ssafy.seoulpop.heritage.repository;

import com.ssafy.seoulpop.heritage.domain.Heritage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeritageRepository extends JpaRepository<Heritage, Long> {

}
