package com.mymicroservice.starter.filter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class TraceIdFilterAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner =
            new WebApplicationContextRunner()
                    .withConfiguration(
                            AutoConfigurations.of(TraceIdFilterAutoConfiguration.class)
                    );

    @Test
    void shouldCreateBeanByDefault() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(TraceIdFilter.class);
        });
    }

    @Test
    void shouldNotCreateBeanWhenDisabled() {
        contextRunner
                .withPropertyValues("mymicroservice.filters.trace-id.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(TraceIdFilter.class);
                });
    }

    @Test
    void shouldUseSpringApplicationName() {
        contextRunner
                .withPropertyValues("spring.application.name=my-test-app")
                .run(context -> {
                    TraceIdFilter filter = context.getBean(TraceIdFilter.class);
                    assertThat(filter).isNotNull();
                });
    }

    @Test
    void shouldBackOffWhenCustomBeanProvided() {
        contextRunner
                .withBean(TraceIdFilter.class,
                        () -> new TraceIdFilter(new TraceIdFilterProperties(), "custom"))
                .run(context -> {
                    assertThat(context).hasSingleBean(TraceIdFilter.class);
                });
    }
}
