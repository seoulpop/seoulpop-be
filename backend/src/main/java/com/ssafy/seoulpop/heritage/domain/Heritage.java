package com.ssafy.seoulpop.heritage.domain;

import com.ssafy.seoulpop.common.BaseEntity;
import com.ssafy.seoulpop.heritage.image.HeritageImage;
import com.ssafy.seoulpop.history.domain.History;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class Heritage extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "history_id")
    private History history;

    @NonNull
    private String type;

    @NonNull
    private String label;

    @NonNull
    @Column(columnDefinition = "datetime(0) default now(0)", nullable = false)
    private LocalDateTime registeredAt;

    @Nullable
    private String era;

    @NonNull
    private String address;

    @NonNull
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @OneToMany(mappedBy = "heritage", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HeritageImage> heritageImages = new ArrayList<>();
}
