package com.bcnc.prices.infrastructure.adapter.in.rest;

import com.bcnc.prices.infrastructure.adapter.in.rest.dto.TokenRequest;
import com.bcnc.prices.infrastructure.adapter.in.rest.dto.TokenResponse;
import com.bcnc.prices.infrastructure.security.JwtTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints para autenticación y generación de tokens JWT")
public class AuthController {

    private final JwtTokenService jwtTokenService;

    public AuthController(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/token")
    @Operation(
        summary = "Generar token JWT",
        description = "Genera un token JWT válido por 1 hora para acceder a los endpoints protegidos de la API"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Token generado exitosamente",
        content = @Content(schema = @Schema(implementation = TokenResponse.class))
    )
    @ApiResponse(
        responseCode = "400",
        description = "Request inválido"
    )
    public ResponseEntity<TokenResponse> generateToken(@Valid @RequestBody TokenRequest request) {
        String token = jwtTokenService.generateToken(request.username());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
