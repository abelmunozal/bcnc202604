package com.bcnc.prices.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Price domain invariants")
class PriceTest {

    private static final LocalDateTime START = LocalDateTime.of(2020, 6, 14, 0, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2020, 12, 31, 23, 59, 59);

    @Test
    @DisplayName("Builds a valid price")
    void buildsValidPrice() {
        assertThatCode(() -> new Price(1L, START, END, 1, 35455L, 0, new BigDecimal("35.50"), "EUR"))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Rejects an end date before the start date")
    void rejectsEndDateBeforeStartDate() {
        assertThatThrownBy(() -> new Price(1L, END, START, 1, 35455L, 0, new BigDecimal("35.50"), "EUR"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("End date");
    }

    @Test
    @DisplayName("Rejects a blank currency")
    void rejectsBlankCurrency() {
        assertThatThrownBy(() -> new Price(1L, START, END, 1, 35455L, 0, new BigDecimal("35.50"), "  "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Currency");
    }

    @Test
    @DisplayName("Rejects a null brand id")
    void rejectsNullBrandId() {
        assertThatThrownBy(() -> new Price(null, START, END, 1, 35455L, 0, new BigDecimal("35.50"), "EUR"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Brand ID");
    }
}
