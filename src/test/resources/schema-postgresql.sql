-- PostgreSQL schema used by the Testcontainers showcase test.
-- It mirrors schema.sql but uses Postgres-native identity syntax (the H2 schema uses
-- AUTO_INCREMENT, which is not valid PostgreSQL) — a concrete example of the dialect
-- differences that an in-memory H2 hides and a real-engine test surfaces.
DROP TABLE IF EXISTS PRICES;

CREATE TABLE PRICES (
    ID BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    BRAND_ID BIGINT NOT NULL,
    START_DATE TIMESTAMP NOT NULL,
    END_DATE TIMESTAMP NOT NULL,
    PRICE_LIST INT NOT NULL,
    PRODUCT_ID BIGINT NOT NULL,
    PRIORITY INT NOT NULL,
    PRICE DECIMAL(19, 2) NOT NULL,
    CURR VARCHAR(3) NOT NULL
);

CREATE INDEX idx_prices_lookup ON PRICES(PRODUCT_ID, BRAND_ID, START_DATE, END_DATE);
