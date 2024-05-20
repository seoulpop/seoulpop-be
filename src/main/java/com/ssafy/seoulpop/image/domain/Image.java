package com.ssafy.seoulpop.image.domain;

import com.ssafy.seoulpop.common.BaseEntity;
import com.ssafy.seoulpop.history.domain.History;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
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
public class Image extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private History history;

    @NonNull
    @Column(columnDefinition = "MEDIUMTEXT")
    private String imageUrl;

    @NonNull
    private String caption;
}
