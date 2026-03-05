package com.kafka01.common.event;

import java.math.BigDecimal;

/**
 * 상품 생성 이벤트
 * @param logId
 * @param productId
 * @param title
 * @param price
 * @param quantity
 */
public record ProductCreatedEvent(
        String logId,
        String productId,
        String title,
        BigDecimal price,
        int quantity
) {
}
