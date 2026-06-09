package com.bcnc.prices.infrastructure.adapter.out.persistence;

import com.bcnc.prices.domain.model.Price;

public class PriceMapper {

    private PriceMapper() {
    }

    public static Price toDomain(PriceEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Price(
            entity.getBrandId(),
            entity.getStartDate(),
            entity.getEndDate(),
            entity.getPriceList(),
            entity.getProductId(),
            entity.getPriority(),
            entity.getPrice(),
            entity.getCurr()
        );
    }
}
