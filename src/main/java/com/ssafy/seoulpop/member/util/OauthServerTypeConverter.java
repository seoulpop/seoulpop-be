package com.ssafy.seoulpop.member.util;

import com.ssafy.seoulpop.member.domain.type.OauthServerType;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;


public class OauthServerTypeConverter implements Converter<String, OauthServerType> {

    @Override
    public OauthServerType convert(@NonNull String source) {
        return OauthServerType.fromName(source);
    }
}