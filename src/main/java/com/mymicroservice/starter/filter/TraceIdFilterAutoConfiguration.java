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
@EnableConfigurationProperties(TraceIdFilterProperties.class)
public class TraceIdFilterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = "mymicroservice.filters.trace-id",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    public TraceIdFilter traceIdFilter(
            TraceIdFilterProperties properties,
            Environment environment) {
        String serviceName = environment.getProperty("spring.application.name", "unknown-service");
        return new TraceIdFilter(properties, serviceName);
    }
}
