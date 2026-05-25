package com.mymicroservice.starter.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class TraceIdFilter extends OncePerRequestFilter {

    private final TraceIdFilterProperties properties;
    private final String serviceName;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        /**
         * Получаем или генерируем traceId
         */
        String traceId = Optional.ofNullable(request.getHeader(properties.getHeaderName()))
                .orElse(UUID.randomUUID().toString());

        log.debug("TraceIdFilter START for URI: {}", ((HttpServletRequest) request).getRequestURI());

        /**
         * Устанавливаем значения в MDC
         */
        MDC.put(properties.getMdcKey(), traceId);
        MDC.put(properties.getServiceNameKey(), serviceName);

        /**
         * Добавляем заголовок в ответ
         */
        response.setHeader(properties.getHeaderName(), traceId);

        /**
         * Логируем входящий запрос (если включено)
         */
        if (properties.isLogRequest()) {
            log.info(properties.getRequestLogFormat(),
                    request.getMethod(),
                    request.getRequestURI(),
                    serviceName);
        }

        try {
            /**
             * Продолжаем цепочку фильтров
             */
            filterChain.doFilter(request, response);
        } finally {
            /**
             * Логируем ответ (если включено)
             */
            if (properties.isLogResponse()) {
                log.info(properties.getResponseLogFormat(),
                        response.getStatus(),
                        serviceName,
                        traceId);
            }
            /**
             * Очищаем MDC
             */
            MDC.clear();
        }
    }
}
