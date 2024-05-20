package com.ssafy.seoulpop.history.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HistoryCategory {

    HERITAGE("문화재"),
    LIBERATION_MOVEMENT("3·1운동"),
    KOREAN_WAR("6·25전쟁");

    private final String value;
}
