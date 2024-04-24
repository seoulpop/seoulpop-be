package com.ssafy.seoulpop.heritage.repository;

import com.ssafy.seoulpop.heritage.image.HeritageImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeritageImageRepository extends JpaRepository<HeritageImage, Long> {

}
