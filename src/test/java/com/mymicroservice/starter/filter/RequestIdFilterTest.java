package com.mymicroservice.starter.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit-тесты для {@link RequestIdFilter}.
 *
 * <p>Проверяется:
 * <ul>
 *     <li>Генерация requestId при отсутствии заголовка</li>
 *     <li>Использование входящего requestId</li>
 *     <li>Заполнение MDC во время обработки запроса</li>
 *     <li>Очистка MDC после завершения запроса</li>
 *     <li>Корректная работа при отключённом логировании</li>
 * </ul>
 *
 * <p>Тесты выполняются без поднятия Spring Context
 * и проверяют только бизнес-логику фильтра.
 */
public class RequestIdFilterTest {

    private final RequestIdFilterProperties properties = new RequestIdFilterProperties();
    private final String serviceName = "test-service";

    /**
     * Очищает MDC после выполнения каждого теста.
     *
     * <p>Необходимо для предотвращения утечки значений MDC
     * между тестами, так как MDC использует ThreadLocal.
     */
    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    /**
     * Проверяет, что при отсутствии заголовка requestId
     * фильтр генерирует новый идентификатор, добавляет его в response header
     * и очищает MDC после завершения обработки.
     *
     * @throws ServletException если произошла ошибка фильтрации
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Test
    void shouldGenerateRequestIdIfHeaderMissing() throws ServletException, IOException {
        RequestIdFilter filter = new RequestIdFilter(properties, serviceName);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        String headerValue = response.getHeader(properties.getHeaderName());

        assertThat(headerValue).isNotNull();
        assertThat(MDC.get(properties.getMdcKey())).isNull();
    }

    /**
     * Проверяет, что если входящий запрос содержит заголовок requestId,
     * фильтр использует его без генерации нового значения
     * и пробрасывает его в response.
     *
     * @throws ServletException если произошла ошибка фильтрации
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Test
    void shouldUseExistingRequestIdFromHeader() throws ServletException, IOException {
        RequestIdFilter filter = new RequestIdFilter(properties, serviceName);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        request.addHeader(properties.getHeaderName(), "custom-id-123");

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(response.getHeader(properties.getHeaderName()))
                .isEqualTo("custom-id-123");
    }

    /**
     * Проверяет, что во время обработки запроса
     * фильтр помещает значения requestId и serviceName в MDC.
     *
     * <p>Проверка выполняется внутри {@link MockFilterChain},
     * что позволяет убедиться, что MDC заполнен именно
     * в момент выполнения цепочки фильтров.
     *
     * @throws ServletException если произошла ошибка фильтрации
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Test
    void shouldPutValuesIntoMdcDuringRequest() throws ServletException, IOException {
        RequestIdFilter filter = new RequestIdFilter(properties, serviceName);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        MockHttpServletResponse response = new MockHttpServletResponse();

        MockFilterChain chain = new MockFilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) {
                assertThat(MDC.get(properties.getMdcKey())).isNotNull();
                assertThat(MDC.get(properties.getServiceNameKey())).isEqualTo(serviceName);
            }
        };

        filter.doFilter(request, response, chain);
    }

    /**
     * Проверяет, что при отключённом логировании фильтр продолжает корректно работать:
     * <ul>
     *     <li>не выбрасывает исключений</li>
     *     <li>добавляет requestId в response</li>
     * </ul>
     *
     * @throws ServletException если произошла ошибка фильтрации
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Test
    void shouldRespectDisabledRequestLogging() throws ServletException, IOException {
        properties.setLogRequest(false);
        properties.setLogResponse(false);

        RequestIdFilter filter = new RequestIdFilter(properties, serviceName);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertThat(response.getHeader(properties.getHeaderName())).isNotNull();
    }
}