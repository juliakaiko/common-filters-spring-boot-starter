package com.mymicroservice.starter.filter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

/*@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(RequestIdFilterProperties.class)
@ConditionalOnProperty(
        prefix = "mymicroservice.filters.request-id",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)*/

@AutoConfiguration
@EnableConfigurationProperties(RequestIdFilterProperties.class)
@ComponentScan(basePackages = "com.mymicroservice.starter.filter")
public class RequestIdFilterAutoConfiguration {

    /**
     * Фильтр для обычных сервисов (без Spring Security)
     */
   /* @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean<RequestIdFilter> requestIdFilterRegistration(
            RequestIdFilterProperties properties,
            Environment environment) {

        String serviceName = environment.getProperty("spring.application.name", "unknown-service");

        FilterRegistrationBean<RequestIdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestIdFilter(properties, serviceName));
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }*/

    /**
     * Фильтр для сервисов с Spring Security.
     * Подключается в SecurityConfig через addFilterBefore
     */
   /* @Bean
    @ConditionalOnMissingBean
    public RequestIdFilter requestIdSecurityFilter(RequestIdFilterProperties properties, Environment environment) {
        String serviceName = environment.getProperty("spring.application.name", "unknown-service");
        return new RequestIdFilter(properties, serviceName);
    }*/
}
