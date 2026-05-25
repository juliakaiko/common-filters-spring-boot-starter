package com.mymicroservice.starter.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mymicroservice.filters.trace-id")
public class TraceIdFilterProperties {

    /**
     * Название заголовка для передачи traceId
     */
    private String headerName = "X-Trace-Id";

    /**
     * Ключ для сохранения traceId в MDC
     */
    private String mdcKey = "traceId";

    /**
     * Ключ для сохранения имени сервиса в MDC
     */
    private String serviceNameKey = "serviceName";

    /**
     * Логировать ли входящий запрос
     */
    private boolean logRequest = true;

    /**
     * Логировать ли ответ
     */
    private boolean logResponse = true;

    /**
     * Формат лога запроса
     */
    private String requestLogFormat = "{} {} - Service: {}";

    /**
     * Формат лога ответа
     */
    private String responseLogFormat = "Response status: {} - Service: {} - TraceId: {}";
}
