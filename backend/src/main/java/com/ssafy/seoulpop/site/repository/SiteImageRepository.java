package com.ssafy.seoulpop.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.seoulpop.site.image.SiteImage;

@Repository
public interface SiteImageRepository extends JpaRepository<SiteImage, Long> {
}
