package com.ssafy.seoulpop.site.repository;

import com.ssafy.seoulpop.site.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

}
