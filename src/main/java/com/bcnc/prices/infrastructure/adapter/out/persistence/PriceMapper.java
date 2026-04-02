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
            entity.getId(),
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
    
    public static PriceEntity toEntity(Price domain) {
        if (domain == null) {
            return null;
        }
        
        return new PriceEntity(
            domain.id(),
            domain.brandId(),
            domain.startDate(),
            domain.endDate(),
            domain.priceList(),
            domain.productId(),
            domain.priority(),
            domain.price(),
            domain.curr()
        );
    }
}
