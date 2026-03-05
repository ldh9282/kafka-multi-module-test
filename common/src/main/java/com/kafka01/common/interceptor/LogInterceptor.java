package com.kafka01.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * 요청 로깅용 인터셉터
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);

    public static final String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, uuid);
        MDC.put(LOG_ID, uuid);

        if (handler instanceof HandlerMethod handlerMethod) {
            String className = handlerMethod.getBeanType().getSimpleName();
            String methodName = handlerMethod.getMethod().getName();
            log.info(">>> Start Controller ::: {}.{}", className, methodName);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();

        String requestURI = request.getRequestURI();
        String uuid = (String) request.getAttribute(LOG_ID);
        if (ex != null) {
            log.error("afterCompletion error!!", ex);
        }
        log.info("<<< execution time ::: {}ms", endTime - startTime);

        if (handler instanceof HandlerMethod handlerMethod) {
            String className = handlerMethod.getBeanType().getSimpleName();
            String methodName = handlerMethod.getMethod().getName();
            log.info("<<< End Controller ::: {}.{}", className, methodName);
        }

        MDC.clear();
    }
}