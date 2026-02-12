package com.mymicroservice.starter.filter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(RequestIdFilterProperties.class)
@ConditionalOnProperty(
        prefix = "mymicroservice.filters.request-id",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public class RequestIdFilterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RequestIdFilter requestIdFilter(
            RequestIdFilterProperties properties,
            Environment environment) {
        String serviceName = environment.getProperty("spring.application.name", "unknown-service");
        return new RequestIdFilter(properties, serviceName);
    }
}
