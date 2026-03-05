package com.kafka01.products.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * 상품생성 요청
 * @param title
 * @param price
 * @param quantity
 */
public record CreateProductRequest(
        @NotNull(message = "제목은 필수 입력 항목입니다.")
        @NotBlank(message = "제목은 공백이 아니어야 합니다.")
        String title,

        @NotNull(message = "가격은 필수 입력 항목입니다.")
        @DecimalMin(value = "0.01", message = "가격은 0.01 이상이어야 합니다.")
        @Digits(integer = 15, fraction = 2, message = "가격은 정수 15자리와 소수점 2자리까지 입력 가능합니다.")
        BigDecimal price,

        @Min(value = 1, message = "상품수량은 1 이상이어야 합니다.")
        int quantity
) {
}
