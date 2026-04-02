package com.bcnc.prices.application.port.in;

import com.bcnc.prices.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface GetApplicablePriceQuery {
    
    Optional<Price> execute(LocalDateTime applicationDate, Long productId, Long brandId);
}
