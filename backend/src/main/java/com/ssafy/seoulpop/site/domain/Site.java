package com.ssafy.seoulpop.site.domain;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.seoulpop.common.BaseEntity;
import com.ssafy.seoulpop.history.domain.History;
import com.ssafy.seoulpop.site.image.SiteImage;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class Site extends BaseEntity {
	@OneToOne
	@JoinColumn(name = "history_id")
	private History history;

	@NonNull
	private String label;

	@NonNull
	private String status;

	@NonNull
	private String summary;

	@NonNull
	@Lob
	private String description;

	@NonNull
	private String historicAddress;

	@NonNull
	private String address;

	@NonNull
	private String reference;

	@OneToMany(mappedBy = "site", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<SiteImage> siteImages = new ArrayList<>();
}
