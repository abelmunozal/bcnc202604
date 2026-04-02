package com.bcnc.prices.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request para generar un token JWT")
public record TokenRequest(
    @Schema(description = "Nombre de usuario", example = "test-user")
    @NotBlank(message = "Username is required")
    String username
) {}
