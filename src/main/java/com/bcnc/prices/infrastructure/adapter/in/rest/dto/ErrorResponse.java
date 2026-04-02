package com.bcnc.prices.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Error response")
public record ErrorResponse(
    
    @Schema(description = "Error message", example = "Price not found for the given criteria")
    String message,
    
    @Schema(description = "HTTP status code", example = "404")
    int status,
    
    @Schema(description = "Timestamp of the error", example = "2020-06-14T10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timestamp
) {
}
