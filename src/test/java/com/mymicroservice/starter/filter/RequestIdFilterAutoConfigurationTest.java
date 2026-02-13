package com.mymicroservice.starter.filter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestIdFilterAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner =
            new WebApplicationContextRunner()
                    .withConfiguration(
                            AutoConfigurations.of(RequestIdFilterAutoConfiguration.class)
                    );

    @Test
    void shouldCreateBeanByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(RequestIdFilter.class);
        });
    }

    @Test
    void shouldNotCreateBeanWhenDisabled() {
        contextRunner
                .withPropertyValues("mymicroservice.filters.request-id.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(RequestIdFilter.class);
                });
    }

    @Test
    void shouldUseSpringApplicationName() {
        contextRunner
                .withPropertyValues("spring.application.name=my-test-app")
                .run(context -> {
                    RequestIdFilter filter = context.getBean(RequestIdFilter.class);
                    assertThat(filter).isNotNull();
                });
    }

    @Test
    void shouldBackOffWhenCustomBeanProvided() {
        contextRunner
                .withBean(RequestIdFilter.class,
                        () -> new RequestIdFilter(new RequestIdFilterProperties(), "custom"))
                .run(context -> {
                    assertThat(context).hasSingleBean(RequestIdFilter.class);
                });
    }
}
