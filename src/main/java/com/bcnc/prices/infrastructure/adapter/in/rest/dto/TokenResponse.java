package com.bcnc.prices.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response con el token JWT generado")
public record TokenResponse(
    @Schema(description = "Token JWT válido por 1 hora", example = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token,
    
    @Schema(description = "Tipo de token", example = "Bearer")
    String tokenType,
    
    @Schema(description = "Tiempo de expiración en segundos", example = "3600")
    Long expiresIn
) {
    public TokenResponse(String token) {
        this(token, "Bearer", 3600L);
    }
}
