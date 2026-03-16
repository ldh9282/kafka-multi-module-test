package com.kafka01.products.dto;

import java.math.BigDecimal;

public record ProductResponse(
        String productId,
        String title,
        BigDecimal price,
        int quantity
) {

}
