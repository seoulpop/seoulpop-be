package com.ssafy.seoulpop.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisDatabaseType {
    TOKEN_DB_IDX,
    BLACKLIST_TOKEN_IDX
}
