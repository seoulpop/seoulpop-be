package com.ssafy.seoulpop.config.database;

import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "secondEntityManagerFactory",
    transactionManagerRef = "secondTransactionManager",
    basePackages = {
        "com.ssafy.seoulpop.history.repository",
        "com.ssafy.seoulpop.heritage.repository",
        "com.ssafy.seoulpop.site.repository"
    }
)
public class SecondDbConfig {

    @Bean(name = "secondDataSource")
    @ConfigurationProperties(prefix = "spring.second-datasource")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSourceInitializer secondDataSourceInitializer(@Qualifier("secondDataSource") DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("data.sql")); // Second DB 전용 SQL 스크립트
        initializer.setDatabasePopulator(populator);
        return initializer;
    }


    @Bean(name = "secondEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
        EntityManagerFactoryBuilder builder,
        @Qualifier("secondDataSource") DataSource dataSource
    ) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create");

        return builder
            .dataSource(dataSource)
            .packages(
                "com.ssafy.seoulpop.history.domain",
                "com.ssafy.seoulpop.heritage.domain",
                "com.ssafy.seoulpop.site.domain",
                "com.ssafy.seoulpop.heritage.image",
                "com.ssafy.seoulpop.site.image"
            ) // Second DB를 사용하는 모델 패키지 경로
            .persistenceUnit("second")
            .properties(properties)
            .build();
    }

    @Bean(name = "secondTransactionManager")
    public PlatformTransactionManager secondTransactionManager(
        @Qualifier("secondEntityManagerFactory") EntityManagerFactory secondEntityManagerFactory
    ) {
        return new JpaTransactionManager(secondEntityManagerFactory);
    }
}
