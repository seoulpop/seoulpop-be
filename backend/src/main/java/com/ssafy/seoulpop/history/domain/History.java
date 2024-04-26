package com.ssafy.seoulpop.history.domain;

import com.ssafy.seoulpop.common.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
public class History extends BaseEntity {

    @NonNull
    private Double lat;

    @NonNull
    private Double lng;

    @NonNull
    private String name;

    @NonNull
    private String category;

    @Embedded
    private Cell cell;
}
