package com.ssafy.seoulpop.atlas.domain;

import com.ssafy.seoulpop.common.BaseEntity;
import com.ssafy.seoulpop.history.domain.History;
import com.ssafy.seoulpop.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Atlas extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private History history;

    @NonNull
    @ColumnDefault("1")
    private Integer visitCnt;

    public void updateVisitCnt() {
        this.visitCnt++;
    }
}
