package br.com.eduardo.jwtvalidator.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class JwtPayloadValidatorTest {

    private final JwtPayloadValidator validator = new JwtPayloadValidator(new ObjectMapper());

    @Test
    void shouldReturnTrueWhenPayloadContainsValidBase64UrlJson() {
        boolean result = validator.isValid("abc.eyJzdWIiOiIxMjMifQ.ghi");

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenPayloadIsInvalidBase64Url() {
        boolean result = validator.isValid("abc.invalid**payload.ghi");

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPayloadIsNotJson() {
        boolean result = validator.isValid("abc.bm90LWpzb24.ghi");

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTokenHasLessThanThreeParts() {
        boolean result = validator.isValid("abc.eyJzdWIiOiIxMjMifQ");

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTokenIsNull() {
        boolean result = validator.isValid(null);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTokenIsBlank() {
        boolean result = validator.isValid("   ");

        assertFalse(result);
    }
}
