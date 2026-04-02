package com.bcnc.prices.infrastructure.adapter.out.persistence;

import com.bcnc.prices.domain.model.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(PriceRepositoryAdapter.class)
@DisplayName("Price Repository Adapter - Priority Resolution Tests")
class PriceRepositoryAdapterTest {
    
    @Autowired
    private PriceRepositoryAdapter priceRepositoryAdapter;
    
    @Test
    @DisplayName("Should return highest priority price when multiple prices match the same date range")
    void shouldReturnHighestPriorityWhenMultiplePricesMatch() {
        LocalDateTime dateInOverlappingRange = LocalDateTime.of(2020, 6, 14, 16, 0, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        
        Optional<Price> result = priceRepositoryAdapter.findApplicablePrice(
            dateInOverlappingRange, 
            productId, 
            brandId
        );
        
        assertThat(result).isPresent();
        assertThat(result.get().priority()).isEqualTo(1);
        assertThat(result.get().priceList()).isEqualTo(2);
        assertThat(result.get().price()).isEqualByComparingTo("25.45");
    }
    
    @Test
    @DisplayName("Should return single price when only one matches")
    void shouldReturnSinglePriceWhenOnlyOneMatches() {
        LocalDateTime dateWithSingleMatch = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        
        Optional<Price> result = priceRepositoryAdapter.findApplicablePrice(
            dateWithSingleMatch, 
            productId, 
            brandId
        );
        
        assertThat(result).isPresent();
        assertThat(result.get().priority()).isEqualTo(0);
        assertThat(result.get().priceList()).isEqualTo(1);
        assertThat(result.get().price()).isEqualByComparingTo("35.50");
    }
    
    @Test
    @DisplayName("Should return empty when no price matches the criteria")
    void shouldReturnEmptyWhenNoPriceMatches() {
        LocalDateTime futureDate = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        
        Optional<Price> result = priceRepositoryAdapter.findApplicablePrice(
            futureDate, 
            productId, 
            brandId
        );
        
        assertThat(result).isEmpty();
    }
    
    @Test
    @DisplayName("Should return empty when product does not exist")
    void shouldReturnEmptyWhenProductDoesNotExist() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0, 0);
        Long nonExistentProductId = 99999L;
        Long brandId = 1L;
        
        Optional<Price> result = priceRepositoryAdapter.findApplicablePrice(
            date, 
            nonExistentProductId, 
            brandId
        );
        
        assertThat(result).isEmpty();
    }
    
    @Test
    @DisplayName("Should handle exact boundary dates correctly - start date")
    void shouldHandleStartDateBoundary() {
        LocalDateTime exactStartDate = LocalDateTime.of(2020, 6, 14, 15, 0, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        
        Optional<Price> result = priceRepositoryAdapter.findApplicablePrice(
            exactStartDate, 
            productId, 
            brandId
        );
        
        assertThat(result).isPresent();
        assertThat(result.get().priceList()).isEqualTo(2);
    }
    
    @Test
    @DisplayName("Should handle exact boundary dates correctly - end date")
    void shouldHandleEndDateBoundary() {
        LocalDateTime exactEndDate = LocalDateTime.of(2020, 6, 14, 18, 30, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        
        Optional<Price> result = priceRepositoryAdapter.findApplicablePrice(
            exactEndDate, 
            productId, 
            brandId
        );
        
        assertThat(result).isPresent();
        assertThat(result.get().priceList()).isEqualTo(2);
    }
}
