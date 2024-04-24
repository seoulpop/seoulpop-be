package com.ssafy.seoulpop.site.repository;

import com.ssafy.seoulpop.site.image.SiteImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteImageRepository extends JpaRepository<SiteImage, Long> {

}
