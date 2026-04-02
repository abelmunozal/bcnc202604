package com.bcnc.prices.application.service;

import com.bcnc.prices.application.port.in.GetApplicablePriceQuery;
import com.bcnc.prices.domain.model.Price;
import com.bcnc.prices.domain.repository.PriceRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class PriceQueryService implements GetApplicablePriceQuery {
    
    private final PriceRepository priceRepository;
    
    public PriceQueryService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }
    
    @Override
    public Optional<Price> execute(LocalDateTime applicationDate, Long productId, Long brandId) {
        if (applicationDate == null) {
            throw new IllegalArgumentException("Application date cannot be null");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (brandId == null) {
            throw new IllegalArgumentException("Brand ID cannot be null");
        }
        
        return priceRepository.findApplicablePrice(applicationDate, productId, brandId);
    }
}
