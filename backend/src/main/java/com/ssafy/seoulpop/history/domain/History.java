package com.ssafy.seoulpop.history.domain;

import com.ssafy.seoulpop.common.BaseEntity;
import com.ssafy.seoulpop.image.domain.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @NonNull
    private String thumbnail;

    @NonNull
    private String address;

    @NonNull
    private String label;

    @NonNull
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private String status; // SiteDto

    private String summary; // SiteDto

    private String historicAddress; // SiteDto

    private String reference; // SiteDto

    @Column(columnDefinition = "datetime(0) default now(0)", nullable = false)
    private LocalDateTime registeredAt; // HeritageDto

    private String era; // HeritageDto

    private String atlasImageUrl; // HeritageDto

    @Embedded
    private Cell cell;

    @OneToMany(mappedBy = "history", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Image> images = new ArrayList<>();
}
