package com.bcnc.prices.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain model of an applicable price (a rate of a brand for a product within a date range).
 * <p>
 * It is a pure value object: it holds no persistence identity (the database surrogate key is
 * an infrastructure concern that lives only in the JPA entity) and enforces its own invariants.
 */
public record Price(
    Long brandId,
    LocalDateTime startDate,
    LocalDateTime endDate,
    Integer priceList,
    Long productId,
    Integer priority,
    BigDecimal price,
    String currency
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
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
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
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or blank");
        }
    }
}
