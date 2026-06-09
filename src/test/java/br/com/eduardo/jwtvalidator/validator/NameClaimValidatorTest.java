package br.com.eduardo.jwtvalidator.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class NameClaimValidatorTest {

    private final NameClaimValidator validator = new NameClaimValidator();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnTrueWhenNameIsValid() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":3}
                """);

        boolean result = validator.isValid(payload);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenNameContainsNumber() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"M4ria Olivia","Role":"Admin","Seed":3}
                """);

        boolean result = validator.isValid(payload);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenNameIsEmpty() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"","Role":"Admin","Seed":3}
                """);

        boolean result = validator.isValid(payload);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenNameIsBlank() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"   ","Role":"Admin","Seed":3}
                """);

        boolean result = validator.isValid(payload);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenNameIsMissing() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Role":"Admin","Seed":3}
                """);

        boolean result = validator.isValid(payload);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenNameIsNull() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":null,"Role":"Admin","Seed":3}
                """);

        boolean result = validator.isValid(payload);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenNameHasMoreThan256Characters() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"%s","Role":"Admin","Seed":3}
                """.formatted("a".repeat(257)));

        boolean result = validator.isValid(payload);

        assertFalse(result);
    }

    @Test
    void shouldReturnTrueWhenNameHasExactly256Characters() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"%s","Role":"Admin","Seed":3}
                """.formatted("a".repeat(256)));

        boolean result = validator.isValid(payload);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenNameIsNotTextualNumber() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":123,"Role":"Admin","Seed":3}
                """);

        boolean result = validator.isValid(payload);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenNameIsNotTextualObject() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":{"first":"Maria"},"Role":"Admin","Seed":3}
                """);

        boolean result = validator.isValid(payload);

        assertFalse(result);
    }
}
