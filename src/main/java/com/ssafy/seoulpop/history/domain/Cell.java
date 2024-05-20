package com.ssafy.seoulpop.history.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cell {

    @Column(nullable = false, name = "cell_7_index")
    private String cell7Index;

    @Column(nullable = false, name = "cell_8_index")
    private String cell8Index;

    @Column(nullable = false, name = "cell_9_index")
    private String cell9Index;

    @Column(nullable = false, name = "cell_10_index")
    private String cell10Index;

    @Column(nullable = false, name = "cell_11_index")
    private String cell11Index;

    @Column(nullable = false, name = "cell_12_index")
    private String cell12Index;
}
