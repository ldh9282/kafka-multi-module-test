package com.kafka01.common.dto;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 공통 응답
 */
public record BaseResponse<T>(
        Header header,
        T body
) {

    public static <T> BaseResponse<T> body(T body) {
        return new BaseResponse<>(Header.from(HttpStatus.OK, null), body);
    }

    public static <T> BaseResponse<T> body(HttpStatus status, T body) {
        return new BaseResponse<>(Header.from(status, null), body);
    }

    public static <T> BaseResponse<T> error(HttpStatus status, String message) {
        return new BaseResponse<>(Header.from(status, message), null);
    }

    /**
     * 헤더 정보를 담당하는 내부 Record
     */
    public record Header(
            boolean ok,
            int status,
            String reason,
            boolean error,
            String errorMsg,
            String auditor,
            String logId,
            String timestamp,
            String formattedTime
    ) {
        public static Header from(HttpStatus status, String errorMsg) {
            LocalDateTime now = LocalDateTime.now();

            return new Header(
                    !status.isError(),
                    status.value(),
                    status.getReasonPhrase(),
                    status.isError(),
                    errorMsg,
                    resolveAuditor(),
                    MDC.get("logId"),
                    now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS")),
                    now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"))
            );
        }

        private static String resolveAuditor() {
            var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return "SYSTEM";
            }
            String user = attributes.getRequest().getHeader("X-USER-ID");
            return (user == null || user.isBlank()) ? "SYSTEM" : user;
        }

    }
}