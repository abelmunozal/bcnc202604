package com.bcnc.prices.infrastructure.adapter.out.persistence;

import com.bcnc.prices.domain.model.Price;
import com.bcnc.prices.domain.repository.PriceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PriceRepositoryAdapter implements PriceRepository {

    /**
     * Disambiguation rule: when several rates overlap the same instant, the highest
     * {@code priority} wins. {@code startDate} and {@code priceList} are added as
     * secondary criteria so the "single result" is fully deterministic on ties.
     */
    private static final Sort BY_PRIORITY = Sort.by(
            Sort.Order.desc("priority"),
            Sort.Order.desc("startDate"),
            Sort.Order.desc("priceList"));

    private final JpaPriceRepository jpaPriceRepository;

    public PriceRepositoryAdapter(JpaPriceRepository jpaPriceRepository) {
        this.jpaPriceRepository = jpaPriceRepository;
    }

    @Override
    public Optional<Price> findApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        return jpaPriceRepository
                .findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        productId, brandId, applicationDate, applicationDate, BY_PRIORITY)
                .map(PriceMapper::toDomain);
    }
}
