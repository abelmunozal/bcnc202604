package com.bcnc.prices.domain.repository;

import com.bcnc.prices.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepository {
    
    Optional<Price> findApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId);
}
