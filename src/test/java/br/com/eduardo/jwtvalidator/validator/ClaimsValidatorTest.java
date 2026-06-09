package br.com.eduardo.jwtvalidator.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class ClaimsValidatorTest {

    private final ClaimsValidator validator = new ClaimsValidator();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnTrueWhenPayloadContainsExactlyRequiredClaims() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Eduardo","Role":"Admin","Seed":3}
                """);

        boolean result = validator.isValid(payload);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenPayloadContainsExtraClaim() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Eduardo","Role":"Admin","Seed":3,"Org":"OpenAI"}
                """);

        boolean result = validator.isValid(payload);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPayloadIsMissingName() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Role":"Admin","Seed":3}
                """);

        boolean result = validator.isValid(payload);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPayloadIsMissingRole() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Eduardo","Seed":3}
                """);

        boolean result = validator.isValid(payload);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPayloadIsMissingSeed() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Eduardo","Role":"Admin"}
                """);

        boolean result = validator.isValid(payload);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPayloadIsEmpty() throws Exception {
        JsonNode payload = objectMapper.readTree("{}");

        boolean result = validator.isValid(payload);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPayloadIsNull() {
        boolean result = validator.isValid(null);

        assertFalse(result);
    }
}
