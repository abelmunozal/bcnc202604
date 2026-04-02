package com.bcnc.prices.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Price(
    Long id,
    Long brandId,
    LocalDateTime startDate,
    LocalDateTime endDate,
    Integer priceList,
    Long productId,
    Integer priority,
    BigDecimal price,
    String curr
) {
    public Price {
        if (brandId == null) {
            throw new IllegalArgumentException("Brand ID cannot be null");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (priceList == null) {
            throw new IllegalArgumentException("Price list cannot be null");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("Price cannot be null");
        }
        if (curr == null || curr.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or blank");
        }
    }
}
