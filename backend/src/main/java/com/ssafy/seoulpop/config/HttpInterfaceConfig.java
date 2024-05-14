package com.ssafy.seoulpop.config;

import com.ssafy.seoulpop.fcm.domain.client.FcmApiClient;
import com.ssafy.seoulpop.member.domain.client.KakaoApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;


@Configuration
public class HttpInterfaceConfig {

    @Bean
    public KakaoApiClient kakaoApiClient() {
        return createHttpInterface(KakaoApiClient.class);
    }

    @Bean
    public FcmApiClient fcmApiClient() {
        return createHttpInterface(FcmApiClient.class);
    }

    private <T> T createHttpInterface(Class<T> c) {
        WebClient webClient = WebClient.create();
        HttpServiceProxyFactory build = HttpServiceProxyFactory.builderFor(
            WebClientAdapter.create(webClient)).build();
        return build.createClient(c);
    }
}
