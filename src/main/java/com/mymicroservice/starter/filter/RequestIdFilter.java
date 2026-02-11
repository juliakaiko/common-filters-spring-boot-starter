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
public class RequestIdFilter extends OncePerRequestFilter {

    private final RequestIdFilterProperties properties;
    private final String serviceName;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Получаем или генерируем requestId
        String requestId = Optional.ofNullable(request.getHeader(properties.getHeaderName()))
                .orElse(UUID.randomUUID().toString());

        // 2. Устанавливаем значения в MDC
        MDC.put(properties.getMdcKey(), requestId);
        MDC.put(properties.getServiceNameKey(), serviceName);

        log.info("RequestIdFilter active: requestId={}, path={}", requestId, request.getRequestURI());

        // 3. Добавляем заголовок в ответ
        response.setHeader(properties.getHeaderName(), requestId);

        // 4. Логируем входящий запрос (если включено)
        if (properties.isLogRequest()) {
            log.info(properties.getRequestLogFormat(),
                    request.getMethod(),
                    request.getRequestURI(),
                    serviceName);
        }

        try {
            // 5. Продолжаем цепочку фильтров
            filterChain.doFilter(request, response);
        } finally {
            // 6. Логируем ответ (если включено)
            if (properties.isLogResponse()) {
                log.info(properties.getResponseLogFormat(),
                        response.getStatus(),
                        serviceName,
                        requestId);
            }

            // 7. Очищаем MDC
            MDC.clear();
        }
    }
}
