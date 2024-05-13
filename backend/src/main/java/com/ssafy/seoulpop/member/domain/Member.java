package com.ssafy.seoulpop.member.domain;

import com.ssafy.seoulpop.atlas.domain.Atlas;
import com.ssafy.seoulpop.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DynamicUpdate
@Table(name = "member", uniqueConstraints = {
    @UniqueConstraint(name = "oauth_id_unique", columnNames = {"oauth_server_id", "oauth_server"})})
public class Member extends BaseEntity {

    @Embedded
    private OauthId oauthId;

    @NonNull
    private String name;

    @NonNull
    @Builder.Default
    private Boolean deleted = false;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Atlas> atlases = new ArrayList<>();
}