package com.bcnc.prices;

import com.bcnc.prices.infrastructure.adapter.in.rest.dto.PriceResponse;
import com.bcnc.prices.infrastructure.adapter.in.rest.dto.TokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke test over the real HTTP stack (random port, real servlet container and JSON
 * serialisation) rather than MockMvc. Verifies the application boots, exposes a healthy
 * actuator endpoint and serves the protected endpoint with a freshly issued JWT end to end.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Application smoke test (real HTTP stack)")
class ApplicationSmokeTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    @DisplayName("Actuator health endpoint reports UP")
    void healthIsUp() {
        ResponseEntity<String> response = rest.getForEntity("/actuator/health", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("UP");
    }

    @Test
    @DisplayName("A token issued by /auth/token grants access to the protected endpoint over HTTP")
    void issuedTokenGrantsAccessOverRealHttp() {
        HttpHeaders jsonHeaders = new HttpHeaders();
        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<TokenResponse> token = rest.postForEntity(
                "/auth/token", new HttpEntity<>("{\"username\":\"smoke-test\"}", jsonHeaders), TokenResponse.class);

        assertThat(token.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(token.getBody()).isNotNull();

        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth(token.getBody().token());
        ResponseEntity<PriceResponse> price = rest.exchange(
                "/api/v1/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1",
                HttpMethod.GET, new HttpEntity<>(authHeaders), PriceResponse.class);

        assertThat(price.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(price.getBody()).isNotNull();
        assertThat(price.getBody().priceList()).isEqualTo(1);
    }
}
