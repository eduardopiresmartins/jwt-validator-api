package br.com.eduardo.jwtvalidator.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import br.com.eduardo.jwtvalidator.dto.JwtValidationRequest;
import br.com.eduardo.jwtvalidator.dto.JwtValidationResponse;
import br.com.eduardo.jwtvalidator.service.JwtValidationService;
import br.com.eduardo.jwtvalidator.validator.JwtPayloadValidator;
import br.com.eduardo.jwtvalidator.validator.JwtStructureValidator;

import com.fasterxml.jackson.databind.ObjectMapper;

class JwtValidationControllerTest {

    private final JwtValidationController controller = new JwtValidationController(
            new JwtValidationService(
                    new JwtStructureValidator(),
                    new JwtPayloadValidator(new ObjectMapper())
            )
    );

    @Test
    void shouldReturnTrueWhenRequestContainsTokenWithValidJsonPayload() {
        ResponseEntity<JwtValidationResponse> response = controller.validate(
                new JwtValidationRequest("abc.eyJzdWIiOiIxMjMifQ.ghi")
        );

        assertTrue(response.getBody().valid());
    }

    @Test
    void shouldReturnFalseWhenRequestContainsTokenWithLessThanThreeParts() {
        ResponseEntity<JwtValidationResponse> response = controller.validate(new JwtValidationRequest("abc.def"));

        assertFalse(response.getBody().valid());
    }

    @Test
    void shouldReturnFalseWhenRequestContainsTokenWithInvalidJsonPayload() {
        ResponseEntity<JwtValidationResponse> response = controller.validate(new JwtValidationRequest("abc.def.ghi"));

        assertFalse(response.getBody().valid());
    }
}
