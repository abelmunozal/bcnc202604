package com.bcnc.prices.infrastructure.config;

import com.bcnc.prices.application.port.in.GetApplicablePriceQuery;
import com.bcnc.prices.application.service.PriceQueryService;
import com.bcnc.prices.domain.repository.PriceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    
    @Bean
    public GetApplicablePriceQuery getApplicablePriceQuery(PriceRepository priceRepository) {
        return new PriceQueryService(priceRepository);
    }
}
