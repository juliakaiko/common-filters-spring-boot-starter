package com.mymicroservice.starter.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mymicroservice.filters.request-id")
public class RequestIdFilterProperties {

    /**
     * Название заголовка для передачи requestId
     */
    private String headerName = "X-Request-Id";

    /**
     * Ключ для сохранения requestId в MDC
     */
    private String mdcKey = "requestId";

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
    private String responseLogFormat = "Response status: {} - Service: {} - RequestId: {}";
}
