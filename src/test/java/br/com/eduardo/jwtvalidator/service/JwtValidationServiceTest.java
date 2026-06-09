package br.com.eduardo.jwtvalidator.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.eduardo.jwtvalidator.validator.JwtPayloadValidator;
import br.com.eduardo.jwtvalidator.validator.JwtStructureValidator;

class JwtValidationServiceTest {

    private final JwtValidationService service = new JwtValidationService(
            new JwtStructureValidator(),
            new JwtPayloadValidator(new ObjectMapper())
    );

    @Test
    void shouldReturnTrueWhenStructureAndPayloadAreValid() {
        boolean result = service.validate("abc.eyJzdWIiOiIxMjMifQ.ghi");

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenStructureIsValidButPayloadIsNotJson() {
        boolean result = service.validate("abc.def.ghi");

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenStructureIsInvalid() {
        boolean result = service.validate("abc.def");

        assertFalse(result);
    }
}
