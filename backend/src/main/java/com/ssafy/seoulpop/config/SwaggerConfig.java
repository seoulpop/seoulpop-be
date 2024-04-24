package com.ssafy.seoulpop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(title = "SEOUL-POP (서울팝)",
        description = "SEOUL-POP api document",
        version = "v1"),
    servers = {@Server(url = "https://api.tempdeploy.site", description = "Default Server URL")})
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/v1/**", "/ping"};

        return GroupedOpenApi.builder()
            .group("SEOUL-POP API v1")
            .pathsToMatch(paths)
            .build();
    }
}
