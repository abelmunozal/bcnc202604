package com.bcnc.prices.infrastructure.adapter.in.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Price Controller Integration Tests")
class PriceControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    private static final Long PRODUCT_ID = 35455L;
    private static final Long BRAND_ID = 1L;
    
    @Test
    @WithMockUser
    @DisplayName("Test 1: Request at 10:00 on day 14 should return price 35.50 EUR (Price List 1)")
    void testPriceAt10AmOnDay14() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", PRODUCT_ID.toString())
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productId", is(PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.brandId", is(BRAND_ID.intValue())))
            .andExpect(jsonPath("$.priceList", is(1)))
            .andExpect(jsonPath("$.price", is(35.50)))
            .andExpect(jsonPath("$.currency", is("EUR")))
            .andExpect(jsonPath("$.startDate", is("2020-06-14T00:00:00")))
            .andExpect(jsonPath("$.endDate", is("2020-12-31T23:59:59")));
    }
    
    @Test
    @WithMockUser
    @DisplayName("Test 2: Request at 16:00 on day 14 should return price 25.45 EUR (Price List 2)")
    void testPriceAt4PmOnDay14() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2020-06-14T16:00:00")
                .param("productId", PRODUCT_ID.toString())
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productId", is(PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.brandId", is(BRAND_ID.intValue())))
            .andExpect(jsonPath("$.priceList", is(2)))
            .andExpect(jsonPath("$.price", is(25.45)))
            .andExpect(jsonPath("$.currency", is("EUR")))
            .andExpect(jsonPath("$.startDate", is("2020-06-14T15:00:00")))
            .andExpect(jsonPath("$.endDate", is("2020-06-14T18:30:00")));
    }
    
    @Test
    @WithMockUser
    @DisplayName("Test 3: Request at 21:00 on day 14 should return price 35.50 EUR (Price List 1)")
    void testPriceAt9PmOnDay14() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2020-06-14T21:00:00")
                .param("productId", PRODUCT_ID.toString())
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productId", is(PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.brandId", is(BRAND_ID.intValue())))
            .andExpect(jsonPath("$.priceList", is(1)))
            .andExpect(jsonPath("$.price", is(35.50)))
            .andExpect(jsonPath("$.currency", is("EUR")))
            .andExpect(jsonPath("$.startDate", is("2020-06-14T00:00:00")))
            .andExpect(jsonPath("$.endDate", is("2020-12-31T23:59:59")));
    }
    
    @Test
    @WithMockUser
    @DisplayName("Test 4: Request at 10:00 on day 15 should return price 30.50 EUR (Price List 3)")
    void testPriceAt10AmOnDay15() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2020-06-15T10:00:00")
                .param("productId", PRODUCT_ID.toString())
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productId", is(PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.brandId", is(BRAND_ID.intValue())))
            .andExpect(jsonPath("$.priceList", is(3)))
            .andExpect(jsonPath("$.price", is(30.50)))
            .andExpect(jsonPath("$.currency", is("EUR")))
            .andExpect(jsonPath("$.startDate", is("2020-06-15T00:00:00")))
            .andExpect(jsonPath("$.endDate", is("2020-06-15T11:00:00")));
    }
    
    @Test
    @WithMockUser
    @DisplayName("Test 5: Request at 21:00 on day 16 should return price 38.95 EUR (Price List 4)")
    void testPriceAt9PmOnDay16() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2020-06-16T21:00:00")
                .param("productId", PRODUCT_ID.toString())
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productId", is(PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.brandId", is(BRAND_ID.intValue())))
            .andExpect(jsonPath("$.priceList", is(4)))
            .andExpect(jsonPath("$.price", is(38.95)))
            .andExpect(jsonPath("$.currency", is("EUR")))
            .andExpect(jsonPath("$.startDate", is("2020-06-15T16:00:00")))
            .andExpect(jsonPath("$.endDate", is("2020-12-31T23:59:59")));
    }
    
    @Test
    @WithMockUser
    @DisplayName("Test: Request with non-existent product should return 404")
    void testPriceNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", "99999")
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("Test: Request without authentication should return 401")
    void testUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2020-06-14T10:00:00")
                .param("productId", PRODUCT_ID.toString())
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    @WithMockUser
    @DisplayName("Test: Request with invalid date format should return 400")
    void testInvalidDateFormat() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "invalid-date")
                .param("productId", PRODUCT_ID.toString())
                .param("brandId", BRAND_ID.toString()))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser
    @DisplayName("Test: Request with missing parameters should return 400")
    void testMissingParameters() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                .param("applicationDate", "2020-06-14T10:00:00"))
            .andExpect(status().isBadRequest());
    }
}
