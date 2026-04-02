package com.bcnc.prices.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Price information response")
public record PriceResponse(
    
    @Schema(description = "Product identifier", example = "35455")
    Long productId,
    
    @Schema(description = "Brand identifier", example = "1")
    Long brandId,
    
    @Schema(description = "Price list identifier", example = "1")
    Integer priceList,
    
    @Schema(description = "Start date of price applicability", example = "2020-06-14T00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime startDate,
    
    @Schema(description = "End date of price applicability", example = "2020-12-31T23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime endDate,
    
    @Schema(description = "Final price to apply", example = "35.50")
    BigDecimal price,
    
    @Schema(description = "Currency code", example = "EUR")
    String currency
) {
}
