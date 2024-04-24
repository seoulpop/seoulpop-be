package com.ssafy.seoulpop.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.seoulpop.site.domain.Site;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
}
