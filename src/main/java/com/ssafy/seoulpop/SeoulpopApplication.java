package com.ssafy.seoulpop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ConfigurationPropertiesScan
@EnableJpaAuditing
public class SeoulpopApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeoulpopApplication.class, args);
    }

}
