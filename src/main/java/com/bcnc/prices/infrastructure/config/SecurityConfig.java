package com.bcnc.prices.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/h2-console/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/v1/prices/**").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(jwtDecoder))
                .authenticationEntryPoint((request, response, authException) -> {
                    // Only enforce authentication on protected endpoints
                    if (request.getRequestURI().startsWith("/api/v1/prices")) {
                        response.sendError(401, "Unauthorized");
                    }
                })
            );

        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    /**
     * Loads a fixed RSA key pair from PEM resources so the tokens issued by
     * {@code /auth/token} stay valid across restarts and can be verified by every running
     * instance (in-memory generation produced a new key on each boot). The bundled keys are
     * for local/development use only; a real deployment would source them from a secret store.
     */
    @Bean
    public KeyPair jwtKeyPair(
            @Value("classpath:certs/jwt-private.pem") Resource privateKeyResource,
            @Value("classpath:certs/jwt-public.pem") Resource publicKeyResource) throws IOException {
        try (InputStream privateKeyStream = privateKeyResource.getInputStream();
             InputStream publicKeyStream = publicKeyResource.getInputStream()) {
            RSAPrivateKey privateKey = RsaKeyConverters.pkcs8().convert(privateKeyStream);
            RSAPublicKey publicKey = RsaKeyConverters.x509().convert(publicKeyStream);
            return new KeyPair(publicKey, privateKey);
        }
    }

    @Bean
    public JwtDecoder jwtDecoder(KeyPair keyPair) {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair.getPublic()).build();
    }
}
