package com.bcnc.prices.infrastructure.adapter.out.persistence;

import com.bcnc.prices.domain.model.Price;
import com.bcnc.prices.domain.repository.PriceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Production-fidelity showcase: runs the price-resolution query against a real PostgreSQL
 * instance (via Testcontainers) instead of in-memory H2, proving the DB-side {@code LIMIT 1}
 * and ordering behave identically on the engine used in production.
 * <p>
 * Marked {@code disabledWithoutDocker} so it is skipped (not failed) where Docker is absent;
 * it runs in CI and on any developer machine with Docker running. The default test suite
 * keeps using H2 as the statement requires — this test does not change that.
 */
@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
@TestPropertySource(properties = {
        "spring.sql.init.schema-locations=classpath:schema-postgresql.sql",
        "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect",
        "spring.datasource.driver-class-name=org.postgresql.Driver"
})
@DisplayName("Price resolution against real PostgreSQL (Testcontainers showcase)")
class PostgresPriceRepositoryShowcaseTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private PriceRepository priceRepository;

    @Test
    @DisplayName("Resolves the highest-priority overlapping rate on PostgreSQL")
    void resolvesHighestPriorityOnPostgres() {
        Optional<Price> result = priceRepository.findApplicablePrice(
                LocalDateTime.of(2020, 6, 14, 16, 0, 0), 35455L, 1L);

        assertThat(result).isPresent();
        assertThat(result.get().priceList()).isEqualTo(2);
        assertThat(result.get().price()).isEqualByComparingTo("25.45");
    }
}
