package br.com.eduardo.jwtvalidator.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class SeedClaimValidatorTest {

    private final SeedClaimValidator validator = new SeedClaimValidator();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnTrueWhenSeedIsPrimeText() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":"7841"}
                """);

        assertTrue(validator.isValid(payload));
    }

    @Test
    void shouldReturnTrueWhenSeedIsPrimeNumber() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":3}
                """);

        assertTrue(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenSeedIsNotPrime() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":4}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenSeedIsZero() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":0}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenSeedIsOne() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":1}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenSeedIsNegative() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":-3}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenSeedIsEmpty() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":""}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenSeedIsBlank() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":"   "}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenSeedIsMissing() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin"}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenSeedIsNull() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":null}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenSeedContainsLetters() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":"78a1"}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenSeedIsDecimal() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":3.14}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenSeedIsObject() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":{"value":3}}
                """);

        assertFalse(validator.isValid(payload));
    }
}
