package com.bcnc.prices.domain.exception;

import java.time.LocalDateTime;

/**
 * Raised when no price is applicable for the requested product, brand and instant.
 * Represents a domain-level "not found" condition, mapped to HTTP 404 by the REST adapter.
 */
public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(LocalDateTime applicationDate, Long productId, Long brandId) {
        super(String.format(
                "No applicable price found for product %d and brand %d at %s",
                productId, brandId, applicationDate));
    }
}
