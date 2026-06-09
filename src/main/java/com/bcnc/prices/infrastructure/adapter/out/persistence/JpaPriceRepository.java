package com.bcnc.prices.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {

    /**
     * Returns the single applicable price for the given product, brand and instant.
     * <p>
     * The {@code findFirst} prefix makes Spring Data apply a dialect-specific {@code LIMIT 1}
     * at the database level, so only the winning row is fetched (no in-memory filtering).
     * The {@link Sort} argument carries the tie-break ordering supplied by the caller.
     */
    Optional<PriceEntity> findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long productId, Long brandId, LocalDateTime startDate, LocalDateTime endDate, Sort sort);
}
