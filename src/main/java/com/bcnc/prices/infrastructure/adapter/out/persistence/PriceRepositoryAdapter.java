package com.bcnc.prices.infrastructure.adapter.out.persistence;

import com.bcnc.prices.domain.model.Price;
import com.bcnc.prices.domain.repository.PriceRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PriceRepositoryAdapter implements PriceRepository {
    
    private final JpaPriceRepository jpaPriceRepository;
    
    public PriceRepositoryAdapter(JpaPriceRepository jpaPriceRepository) {
        this.jpaPriceRepository = jpaPriceRepository;
    }
    
    @Override
    public Optional<Price> findApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        return jpaPriceRepository
            .findApplicablePrices(applicationDate, productId, brandId)
            .stream()
            .findFirst()
            .map(PriceMapper::toDomain);
    }
}
