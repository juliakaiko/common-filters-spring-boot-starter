package com.mymicroservice.starter.filter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
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

    /*@Bean
    @ConditionalOnMissingBean(name = "requestIdFilter")
    public RequestIdFilter requestIdFilter(RequestIdFilterProperties properties) {
        return new RequestIdFilter(properties);
    }*/

    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean<RequestIdFilter> requestIdFilter(
            RequestIdFilterProperties properties,
            Environment environment) {

        String serviceName =
                environment.getProperty("spring.application.name", "unknown-service");

        FilterRegistrationBean<RequestIdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestIdFilter(properties, serviceName));
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return registration;
    }
}
