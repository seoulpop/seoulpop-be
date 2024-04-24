package com.ssafy.seoulpop.heritage.image;

import com.ssafy.seoulpop.common.BaseEntity;
import com.ssafy.seoulpop.heritage.domain.Heritage;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class HeritageImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heritage_id")
    private Heritage heritage;

    @NonNull
    @Lob
    private String imageUrl;

    @NonNull
    private String caption;
}
