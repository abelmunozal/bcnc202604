package com.bcnc.prices.infrastructure.adapter.in.rest;

import com.bcnc.prices.application.port.in.GetApplicablePriceQuery;
import com.bcnc.prices.domain.exception.PriceNotFoundException;
import com.bcnc.prices.domain.model.Price;
import com.bcnc.prices.infrastructure.adapter.in.rest.dto.PriceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/prices")
@Tag(name = "Prices", description = "Price management API")
@Validated
public class PriceController {
    
    private final GetApplicablePriceQuery getApplicablePriceQuery;
    
    public PriceController(GetApplicablePriceQuery getApplicablePriceQuery) {
        this.getApplicablePriceQuery = getApplicablePriceQuery;
    }
    
    @Operation(
        summary = "Get applicable price",
        description = "Retrieves the applicable price for a product and brand at a specific date and time. " +
                      "If multiple prices match, the one with highest priority is returned. " +
                      "Date format must be ISO-8601 without timezone (e.g., 2020-06-14T10:00:00)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Price found successfully",
            content = @Content(schema = @Schema(implementation = PriceResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No price found for the given criteria",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
        )
    })
    @GetMapping
    public ResponseEntity<PriceResponse> getApplicablePrice(
        @Parameter(
            description = "Application date and time in ISO-8601 format without timezone (yyyy-MM-dd'T'HH:mm:ss)",
            example = "2020-06-14T10:00:00",
            required = true
        )
        @RequestParam(required = true) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
        LocalDateTime applicationDate,
        
        @Parameter(description = "Product identifier", example = "35455", required = true)
        @RequestParam(required = true)
        @Min(value = 1, message = "productId must be a positive value")
        Long productId,

        @Parameter(description = "Brand identifier (chain)", example = "1", required = true)
        @RequestParam(required = true)
        @Min(value = 1, message = "brandId must be a positive value")
        Long brandId
    ) {
        PriceResponse response = getApplicablePriceQuery
            .execute(applicationDate, productId, brandId)
            .map(this::toPriceResponse)
            .orElseThrow(() -> new PriceNotFoundException(applicationDate, productId, brandId));
        return ResponseEntity.ok(response);
    }
    
    private PriceResponse toPriceResponse(Price price) {
        return new PriceResponse(
            price.productId(),
            price.brandId(),
            price.priceList(),
            price.startDate(),
            price.endDate(),
            price.price(),
            price.currency()
        );
    }
}
