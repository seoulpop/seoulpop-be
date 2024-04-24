package com.ssafy.seoulpop.heritage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.seoulpop.heritage.image.HeritageImage;

@Repository
public interface HeritageImageRepository extends JpaRepository<HeritageImage, Long> {
}
