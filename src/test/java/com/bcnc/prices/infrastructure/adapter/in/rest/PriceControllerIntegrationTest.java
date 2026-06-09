package com.bcnc.prices.infrastructure.adapter.in.rest;

import com.bcnc.prices.infrastructure.adapter.in.rest.dto.PriceResponse;
import com.bcnc.prices.infrastructure.adapter.in.rest.dto.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Price Controller Integration Tests")
class PriceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long PRODUCT_ID = 35455L;
    private static final Long BRAND_ID = 1L;

    /**
     * The five scenarios mandated by the statement: (applicationDate, expected price list,
     * expected price, expected start date, expected end date).
     */
    static Stream<Arguments> mandatoryScenarios() {
        return Stream.of(
            Arguments.of("2020-06-14T10:00:00", 1, "35.50", "2020-06-14T00:00:00", "2020-12-31T23:59:59"),
            Arguments.of("2020-06-14T16:00:00", 2, "25.45", "2020-06-14T15:00:00", "2020-06-14T18:30:00"),
            Arguments.of("2020-06-14T21:00:00", 1, "35.50", "2020-06-14T00:00:00", "2020-12-31T23:59:59"),
            Arguments.of("2020-06-15T10:00:00", 3, "30.50", "2020-06-15T00:00:00", "2020-06-15T11:00:00"),
            Arguments.of("2020-06-16T21:00:00", 4, "38.95", "2020-06-15T16:00:00", "2020-12-31T23:59:59")
        );
    }

    @ParameterizedTest(name = "request at {0} -> price list {1} ({2} EUR)")
    @MethodSource("mandatoryScenarios")
    @WithMockUser
    @DisplayName("Returns the applicable price for the requested instant")
    void returnsApplicablePrice(String applicationDate, int expectedPriceList, String expectedPrice,
                                String expectedStartDate, String expectedEndDate) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", applicationDate)
                .param("productId", PRODUCT_ID.toString())
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isOk())
            .andReturn();

        PriceResponse body = objectMapper.readValue(result.getResponse().getContentAsString(), PriceResponse.class);

        assertThat(body.productId()).isEqualTo(PRODUCT_ID);
        assertThat(body.brandId()).isEqualTo(BRAND_ID);
        assertThat(body.priceList()).isEqualTo(expectedPriceList);
        assertThat(body.price()).isEqualByComparingTo(new BigDecimal(expectedPrice));
        assertThat(body.currency()).isEqualTo("EUR");
        assertThat(body.startDate()).isEqualTo(LocalDateTime.parse(expectedStartDate));
        assertThat(body.endDate()).isEqualTo(LocalDateTime.parse(expectedEndDate));
    }

    @Test
    @DisplayName("A token issued by /auth/token grants access to the protected endpoint")
    void authenticatedRequestWithRealJwtSucceeds() throws Exception {
        String tokenBody = mockMvc.perform(post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"test-user\"}"))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        TokenResponse token = objectMapper.readValue(tokenBody, TokenResponse.class);

        mockMvc.perform(get("/api/v1/prices")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.token())
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", PRODUCT_ID.toString())
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.priceList", is(1)));
    }

    @Test
    @WithMockUser
    @DisplayName("Request with non-existent product returns 404 with an error body")
    void returnsNotFoundWithBody() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "99999")
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status", is(404)))
            .andExpect(jsonPath("$.message", containsString("99999")))
            .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    @Test
    @DisplayName("Request without authentication returns 401")
    void returnsUnauthorizedWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", PRODUCT_ID.toString())
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("Request with invalid date format returns 400")
    void returnsBadRequestForInvalidDate() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "invalid-date")
                .param("productId", PRODUCT_ID.toString())
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Request with missing parameters returns 400")
    void returnsBadRequestForMissingParameters() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2020-06-14T10:00:00"))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Request with a non-positive productId returns 400")
    void returnsBadRequestForNonPositiveProductId() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "-1")
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", is(400)));
    }
}
