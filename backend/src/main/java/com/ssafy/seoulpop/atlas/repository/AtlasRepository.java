package com.ssafy.seoulpop.atlas.repository;

import com.ssafy.seoulpop.atlas.domain.Atlas;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtlasRepository extends JpaRepository<Atlas, Long>, AtlasRepositoryCustom {

    Optional<Atlas> findByMember_IdAndHistory_Id(Long memberId, Long historyId);
}
