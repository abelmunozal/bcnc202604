package com.bcnc.prices.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaPriceRepository extends JpaRepository<PriceEntity, Long> {
    
    @Query("SELECT p FROM PriceEntity p " +
           "WHERE p.productId = :productId " +
           "AND p.brandId = :brandId " +
           "AND p.startDate <= :applicationDate " +
           "AND p.endDate >= :applicationDate " +
           "ORDER BY p.priority DESC")
    List<PriceEntity> findApplicablePrices(
        @Param("applicationDate") LocalDateTime applicationDate,
        @Param("productId") Long productId,
        @Param("brandId") Long brandId
    );
}
