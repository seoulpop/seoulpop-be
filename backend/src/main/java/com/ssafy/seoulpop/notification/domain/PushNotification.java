package com.ssafy.seoulpop.notification.domain;

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
public class PushNotification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private History history;

    @NonNull
    private String title;

    @NonNull
    private String body;

    @NonNull
    @ColumnDefault("false")
    private Boolean checked;

    public void updateRead() {
        this.checked = !this.checked;
    }
}
