package br.com.eduardo.jwtvalidator.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class RoleClaimValidatorTest {

    private final RoleClaimValidator validator = new RoleClaimValidator();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnTrueWhenRoleIsAdmin() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Admin","Seed":3}
                """);

        assertTrue(validator.isValid(payload));
    }

    @Test
    void shouldReturnTrueWhenRoleIsMember() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Member","Seed":3}
                """);

        assertTrue(validator.isValid(payload));
    }

    @Test
    void shouldReturnTrueWhenRoleIsExternal() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"External","Seed":3}
                """);

        assertTrue(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenRoleIsInvalid() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"Guest","Seed":3}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenRoleIsEmpty() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"","Seed":3}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenRoleIsBlank() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"   ","Seed":3}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenRoleIsMissing() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Seed":3}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenRoleIsNull() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":null,"Seed":3}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenRoleIsNotTextualNumber() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":123,"Seed":3}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenRoleIsNotTextualObject() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":{"name":"Admin"},"Seed":3}
                """);

        assertFalse(validator.isValid(payload));
    }

    @Test
    void shouldReturnFalseWhenRoleHasDifferentCase() throws Exception {
        JsonNode payload = objectMapper.readTree("""
                {"Name":"Maria Olivia","Role":"admin","Seed":3}
                """);

        assertFalse(validator.isValid(payload));
    }
}
