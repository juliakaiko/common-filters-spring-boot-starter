package com.mymicroservice.starter.filter.integration;

import com.mymicroservice.starter.filter.RequestIdFilterAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(RequestIdFilterAutoConfiguration.class)
class RequestIdFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAddRequestIdHeaderToResponse() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Request-Id"));
    }

    @Test
    void shouldPropagateExistingRequestId() throws Exception {
        mockMvc.perform(get("/hello")
                        .header("X-Request-Id", "integration-test-id"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Request-Id", "integration-test-id"));
    }

    @Configuration
    static class TestConfig {

        @RestController
        static class TestController {

            @GetMapping("/hello")
            public String hello() {
                return "ok";
            }
        }
    }
}